package com.powernode;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/25 9:36
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAdminServer
public class AdminServerApp {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApp.class);
    }
}
