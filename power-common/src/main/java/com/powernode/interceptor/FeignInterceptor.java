package com.powernode.interceptor;

import com.powernode.constant.TokenConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign权限调用配置
 * 1、一种是服务与服务之间调用 通过获取请求中的token信息然后feign调用时存入requestTemplate的header中
 * 2、一种是将调用信息发送到消息队列，然后再通过feign调用。这时就需要想授权服务申请token
 *
 * @author DuBo
 * @createDate 2022/7/25 16:36
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null){
            HttpServletRequest request = requestAttributes.getRequest();
            String token = request.getHeader(TokenConstant.AUTHORIZATION);
            if (StringUtils.hasText(token)){
                // 传递给下一次请求
                requestTemplate.header(TokenConstant.AUTHORIZATION,token);

            }
        }else {
            // mq的请求场景 还是其他回调场景
            requestTemplate.header(TokenConstant.AUTHORIZATION, TokenConstant.BEARER+ TokenConstant.TOKEN);
        }


    }
}
