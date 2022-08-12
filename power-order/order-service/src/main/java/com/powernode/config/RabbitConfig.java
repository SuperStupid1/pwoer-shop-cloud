package com.powernode.config;

import com.powernode.constant.OrderConstant;
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
 * 队列声明
 *
 * @author DuBo
 * @createDate 2022/8/12 16:53
 */
@Configuration
public class RabbitConfig {
    /**
     * 声明订单超时延迟队列
     *
     * @return
     */
    @Bean
    public Queue orderTimeoutDelayQueue() {
        Map<String, Object> map = new HashMap<>(10);
        // 设置延迟的时间 设置消息死亡后走哪个交换机  走哪个路由key
        map.put("x-message-ttl", 1000 * 60 * 15);
        map.put("x-dead-letter-exchange", OrderConstant.ORDER_TIMEOUT_DEAD_EXCHANGE);
        map.put("x-dead-letter-routing-key", OrderConstant.ORDER_TIMEOUT_DEAD_ROUTING_KEY);
        return new Queue(QueueConstant.ORDER_OVERTIME_DELAY_QUEUE, true, false, false, map);
    }

    /**
     * 声明超时订单消息死信队列
     */
    @Bean
    public Queue deadQueue(){
        return new Queue(OrderConstant.ORDER_TIMEOUT_DEAD_QUEUE);
    }

    /**
     * 声明超时订单消息死信交换机
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(OrderConstant.ORDER_TIMEOUT_DEAD_EXCHANGE);
    }

    /**
     * 绑定死信交换机和死信队列以及路由key
     */
    @Bean
    public Binding deadBind(){
        return BindingBuilder.bind(deadQueue()).to(directExchange()).with(OrderConstant.ORDER_TIMEOUT_DEAD_ROUTING_KEY);
    }

}
