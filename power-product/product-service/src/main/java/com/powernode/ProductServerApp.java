package com.powernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/25 17:26
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ProductServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ProductServerApp.class);
    }
}
