package com.powernode.config;

import com.powernode.constant.QueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列声明
 *
 * @author DuBo
 * @createDate 2022/8/12 16:53
 */
@Configuration
public class RabbitConfig {

    /**
     * 声明es快速导入的队列
     * @return
     */
    @Bean
    public Queue esChangQueue(){
        return new Queue(QueueConstant.ES_CHANGE_QUEUE);
    }



}
