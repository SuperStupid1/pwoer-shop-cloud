package com.powernode.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powernode.constan.GatewayConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * jwt检测
 * @author DuBo
 * @createDate 2022/7/22 19:44
 */
@Order(1)
@Component
public class JwtCheckFilter implements GlobalFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * token校验
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // 判断请求路径是否为放行路径
        if (GatewayConstant.ALLOW_URLS.contains(path)){
            return chain.filter(exchange);
        }
        // 获取请求头中的token信息
        String token = request.getHeaders().getFirst(GatewayConstant.AUTHORIZATION);
        if (StringUtils.hasText(token) && redisTemplate.hasKey(token) ){
                // 鉴权todo
                return chain.filter(exchange);
        }
        // 无token 进行拦截
        ServerHttpResponse response = exchange.getResponse();
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("msg","未授权");
        byte[] bytes ={};
        try {
            bytes = new ObjectMapper().writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DataBuffer data = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(data));
    }
}

