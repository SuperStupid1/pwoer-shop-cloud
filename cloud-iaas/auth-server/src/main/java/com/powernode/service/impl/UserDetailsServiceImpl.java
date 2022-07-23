package com.powernode.service.impl;

import com.powernode.constant.AuthConstant;
import com.powernode.domain.SysLoginUser;
import com.powernode.mapper.SysLoginUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/22 20:48
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysLoginUserMapper sysLoginUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 因为有后台管理系统用户和前端站点用户，所以需要对登录用户进行判断
        // 获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取判断标识
        String loginType = request.getHeader(AuthConstant.LOGIN_TYPE);
        if (StringUtils.isEmpty(loginType)){
            return null;
        }
        // 判断标识类型
        switch (loginType){
            case AuthConstant.SYS_USER:
                // 后台管理系统用户
                SysLoginUser sysLoginUser = sysLoginUserMapper.getByUsername(username);
                if (!ObjectUtils.isEmpty(sysLoginUser)){
                    List<String> auths = sysLoginUserMapper.selectAuthById(sysLoginUser.getUserId());
                    sysLoginUser.setAuths(auths);
                }
                return sysLoginUser;
            case AuthConstant.USER:
                // 前台用户 todo
                break;
            default:
                return null;
        }
        return null;

    }
}
