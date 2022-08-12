package com.powernode.listener;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.powernode.constant.MessageConstant;
import com.powernode.constant.QueueConstant;
import com.powernode.domain.WxMsgDto;
import com.powernode.domain.WxMsgVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/12 16:01
 */
@Component
public class WxMsgListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${wx.template-id}")
    private String templateId;

    @Value("${wx.wx-msg-url}")
    private String msgUrl;

    @RabbitListener(queues = QueueConstant.WX_MSG_DEAD_LETTER_QUEUE)
    public void sendMsg(Message message, Channel channel) {
        String msgBody = new String(message.getBody());
        WxMsgDto wxMsgDto = JSON.parseObject(msgBody, WxMsgDto.class);
        // 封装微信通知 vo
        WxMsgVo wxMsgVo = new WxMsgVo();
        wxMsgVo.setToUser(wxMsgDto.getUserId());
        wxMsgVo.setTemplateId(templateId);
        wxMsgVo.setUrl("http://weixin.qq.com/download");
        wxMsgVo.setTopColor("#FF0000");
        HashMap<String, String> map = new HashMap<>(3);
        map.put("orderSn",wxMsgDto.getOrderSn());
        map.put("productNums",wxMsgDto.getProductNums().toString());
        map.put("total",wxMsgDto.getTotal().toString());
        Map<String, HashMap<String, String>> data = new HashMap<>(1);
        data.put("data",map);
        wxMsgVo.setData(data);
        // 发送通知
        String token = redisTemplate.opsForValue().get("access_token");
        String msgUrl = String.format(this.msgUrl, token);
        HttpUtil.post(msgUrl,JSON.toJSONString(wxMsgVo));
        // 签收消息
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
