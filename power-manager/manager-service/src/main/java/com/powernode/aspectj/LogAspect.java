package com.powernode.aspectj;

import com.alibaba.fastjson.JSON;
import com.powernode.annotation.Log;
import com.powernode.domain.SysLog;
import com.powernode.domain.SysUser;
import com.powernode.service.SysLogService;
import com.powernode.service.SysUserService;
import com.powernode.util.ManagerThreadPoolUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 日志切面
 *
 * @author DuBo
 * @createDate 2022/7/26 17:50
 */
@Component
@Aspect
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysUserService sysUserService;

    @Around("@annotation(com.powernode.annotation.Log)")
    public Object logAround(ProceedingJoinPoint joinPoint) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取注解的operation属性
        Log log = method.getAnnotation(Log.class);
        String operation = log.operation();
        // 获取方法名
        String methodName = method.getName();
        // 获取方法参数
        Object[] joinPointArgs = joinPoint.getArgs();
        String args = joinPointArgs == null ? "" : JSON.toJSONString(joinPointArgs);
        // 获取方法执行时间
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long end = System.currentTimeMillis();
        Long time = end - start;
        // 获取ip
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = "";
        if (!ObjectUtils.isEmpty(requestAttributes)){
            ip = requestAttributes.getRequest().getRemoteAddr();
        }
        // 获取登录用户名
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        // 因为方法非常多所以使用多线程异步处理
        ThreadPoolExecutor threadPool = ManagerThreadPoolUtil.getThreadPool();
        String finalIp = ip;
        threadPool.execute(() -> {
            SysUser loginUser = sysUserService.getById(userId);
            // 保存到数据库
            SysLog sysLog = SysLog.builder()
                    .username(loginUser.getUsername())
                    .operation(operation)
                    .method(methodName)
                    .params(args)
                    .time(time)
                    .ip(finalIp)
                    .createDate(LocalDateTime.now())
                    .build();
            sysLogService.save(sysLog);
        });
        return result;
    }
}
