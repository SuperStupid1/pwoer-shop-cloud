package com.powernode.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.powernode.constan.GatewayConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;

/**
 * 授权存储jwt路由
 *
 * @author DuBo
 * @createDate 2022/7/22 19:46
 */
@Configuration
public class LoginRouter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 使用代码方式的路由做token的存储
     *   前端应用-------->gateway-------->auth-server
     *   auth-server---token---->gateway---->redis----->前端应用
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-server-id",r->r.path("/oauth/token")
                .filters(f->f.modifyResponseBody(String.class,String.class,(exchange,result)->{
                    // result就是auth-server响应过来的值
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject.containsKey(GatewayConstant.ACCESS_TOKEN)){
                        // 获取授权成功的token和过期时间
                        String token = jsonObject.getString(GatewayConstant.ACCESS_TOKEN);
                        Long expireTime = jsonObject.getLong(GatewayConstant.EXPIRES_IN);
                        // 存入redis
                        redisTemplate.opsForValue().set(token+GatewayConstant.BEARER,"",expireTime);
                    }

                    return Mono.just(result);
                })).uri("lb://auth-server")).build();
    }
}
