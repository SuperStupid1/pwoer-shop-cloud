package com.powernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/25 17:26
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ManagerApp {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApp.class);
    }
}
