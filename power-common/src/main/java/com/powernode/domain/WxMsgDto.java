package com.powernode.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 发送到延迟队列的支付提示微信通知封装
 *
 * @author DuBo
 * @createDate 2022/8/12 15:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMsgDto {

    private String userId;
    private String orderSn;
    private Integer productNums;
    private BigDecimal total;

}

