package com.powernode.config;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/12 16:26
 */
@Configuration
public class WxMsgConfig implements ApplicationRunner {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Value("${wx.wx-token-url}")
    private String tokenUrl;

    /**
     * 获取token
     */
    @Scheduled(initialDelay = 1000 * 7100, fixedRate = 1000 * 7100)
    private void getTokenUrl() {
        String tokenUrl = String.format(this.tokenUrl, appId, appSecret);
        String result = HttpUtil.get(tokenUrl);
        JSONObject jsonObject = JSON.parseObject(result);
        String token = jsonObject.getString("access_token");
        Long expire = jsonObject.getLong("expires_in");
        redisTemplate.opsForValue().set("access_token", token, Duration.ofSeconds(expire));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        getTokenUrl();
    }
}
