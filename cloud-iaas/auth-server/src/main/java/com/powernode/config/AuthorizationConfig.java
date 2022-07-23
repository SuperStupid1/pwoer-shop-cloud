package com.powernode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * 授权
 *
 * @author DuBo
 * @createDate 2022/7/23 16:29
 */
@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    /**
     * 第三方应用的配置，只有配置了第三方应用，才能访问授权服务器
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
       clients.inMemory()
               //创建一个web第三方
               .withClient("web")
               //密码
               .secret(passwordEncoder.encode("web-secret"))
               //作用域
               .scopes("all")
               //授权方式一共有四种，验证码授权，密码授权，静默授权，客户端授权
               // 给我们的前端应用授权
               .authorizedGrantTypes("password")
               //访问成功后的跳转地址
               .redirectUris("https://www.baidu.com")
               //token的过期时间
               .accessTokenValiditySeconds(7200)
               .and()
               //创建一个web第三方
               .withClient("client")
               //密码
               .secret(passwordEncoder.encode("client-secret"))
               //作用域
               .scopes("all")
               //授权方式一共有四种，验证码授权，密码授权，静默授权，客户端授权
               // 使用客户端授权 颁发一个应用的全局token 为了在服务之间做特殊传递的
               .authorizedGrantTypes("client_credentials")
               //访问成功后的跳转地址
               .redirectUris("https://www.baidu.com")
               //token的过期时间
               .accessTokenValiditySeconds(Integer.MAX_VALUE)
               .and()
               .withClient("code")
               .secret(passwordEncoder.encode("code-secret"))
               .scopes("all")
               .authorizedGrantTypes("authorization_code")
               .redirectUris("https://www.baidu.com")
               .accessTokenValiditySeconds(7200);
    }

    /**
     * token的存储方式 使用jwt
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jwtTokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager);
    }


    @Bean
    public TokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 使用非对称加密，只要私钥不泄密，别人无法生成token，公钥只能验证token
        // 将文件加载进来
        ClassPathResource resource = new ClassPathResource("db-jwt.jks");
        // 得到keyStore
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(resource, "dubo123".toCharArray());
        KeyPair keyPair = keyFactory.getKeyPair("db-jwt");
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }
}
