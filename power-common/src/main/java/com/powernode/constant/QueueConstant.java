package com.powernode.constant;

/**
 * 公共模块队列常量类
 *
 * @author DuBo
 * @createDate 2022/8/2 18:17
 */
public interface QueueConstant {

    /**
     * es的快速导入的监听队列
     */
    String ES_CHANGE_QUEUE = "es.change.queue";

    /**
     * 订单超时延迟队列
     */
    String ORDER_OVERTIME_DELAY_QUEUE = "order_overtime_delay_queue";

    /**
     * 微信通知延迟队列
     */
    String WX_MSG_DELAY_QUEUE = "wx_msg_delay_queue";

    /**
     * 微信通知消息死信队列
     */
    String WX_MSG_DEAD_LETTER_QUEUE = "wx_msg_dead_letter_queue";
}
