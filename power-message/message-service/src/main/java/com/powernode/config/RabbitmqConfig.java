package com.powernode.config;

import com.powernode.constant.MessageConstant;
import com.powernode.constant.QueueConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/12 15:42
 */
@Configuration
public class RabbitmqConfig {

    /**
     * 声明微信消息延迟队列
     */
    @Bean
    public Queue wxMsgDelayQueue(){
        Map<String,Object> map = new HashMap<>(10);
        // 设置延迟的时间 设置消息死亡后走哪个交换机  走哪个路由key
        map.put("x-message-ttl",1000*60*4);
        map.put("x-dead-letter-exchange",MessageConstant.WX_MSG_DEAD_EXCHANGE);
        map.put("x-dead-letter-routing-key",MessageConstant.WX_MSG_DEAD_KEY);
        return new Queue(QueueConstant.WX_MSG_DELAY_QUEUE,true,false,false,map);
    }

    /**
     * 声明微信消息死信队列
     */
    @Bean
    public Queue deadQueue(){
        return new Queue(QueueConstant.WX_MSG_DEAD_LETTER_QUEUE);
    }

    /**
     * 声明微信消息死信交换机
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MessageConstant.WX_MSG_DEAD_EXCHANGE);
    }

    /**
     * 绑定死信交换机和死信队列以及路由key
     */
    @Bean
    public Binding deadBind(){
        return BindingBuilder.bind(deadQueue()).to(directExchange()).with(MessageConstant.WX_MSG_DEAD_KEY);
    }

}
