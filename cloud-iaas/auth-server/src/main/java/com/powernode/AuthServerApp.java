package com.powernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.client.RestTemplate;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/22 20:34
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAuthorizationServer //开启授权服务器
public class AuthServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApp.class);
    }

}
