package com.powernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 杜波
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class MessageServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApp.class, args);
    }
}