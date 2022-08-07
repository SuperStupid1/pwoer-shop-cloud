package com.powernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 杜波
 *
 * 因为需要使用到mybatisplus的内置对象但是又不需要他的数据源自动配置类 所以剔除掉
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class SearchServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApp.class, args);
    }
}