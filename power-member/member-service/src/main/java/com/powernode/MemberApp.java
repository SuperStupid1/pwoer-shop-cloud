package com.powernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/4 17:47
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MemberApp {
    public static void main(String[] args) {
        SpringApplication.run(MemberApp.class);
    }
}
