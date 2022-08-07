package com.powernode.constan;

import sun.awt.HKSCS;

import java.util.Arrays;
import java.util.List;

/**
 * gateway模块常量类
 *
 * @author DuBo
 * @createDate 2022/7/22 19:59
 */
public interface GatewayConstant {

    /**
     * 授权模块传过来的token key
     */
    String ACCESS_TOKEN = "access_token";

    /**
     * 授权模块传过来的token过期时间key
     */
    String EXPIRES_IN = "expires_in";

    /**
     * 需要放行的路径集合
     */
    List<String> ALLOW_URLS = Arrays.asList("/oauth/token","swagger-ui.html");

    /**
     * 请求头中token信息的前缀
     */
    String BEARER = "bearer ";

    /**
     * 请求头中token信息key
     */
    String AUTHORIZATION = "Authorization";



}
