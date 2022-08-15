package com.powernode.config;

import cn.hutool.core.io.FileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 统一解析token
 * @author DuBo
 * @createDate 2022/7/25 16:13
 */
@Configuration
@EnableResourceServer // 标识为资源服务
// 开启方法级权限认证 prePostEnabled = true 会解锁 @PreAuthorize 和 @PostAuthorize 两个注解。@PreAuthorize
// 注解会在方法执行前进行验证，而 @PostAuthorize 注解会在方法执行后进行验证。

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceConfig extends ResourceServerConfigurerAdapter {
    /**
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(jwtTokenStore());
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtTokenConverter());
    }

    /**
     * 配置公钥
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        ClassPathResource resource = new ClassPathResource("publicKey.txt");
        String publicKey = null;
        try {
            publicKey = FileUtil.readString(resource.getFile(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        jwtAccessTokenConverter.setVerifierKey(publicKey);
        return jwtAccessTokenConverter;
    }

    /**
     *  关于http请求 放行之类的配置
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().disable();
        http.authorizeRequests()
                // swagger  druid ...
                .antMatchers("/v2/api-docs",
                        "/v3/api-docs",
                        //用来获取支持的动作
                        "/swagger-resources/configuration/ui",
                        //用来获取api-docs的URI
                        "/swagger-resources",
                        //安全选项
                        "/swagger-resources/configuration/security",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/druid/**",
                        "/actuator/**",
                        "/p/order/payNotify/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
