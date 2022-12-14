package com.powernode.service;

import com.powernode.domain.OrderPayDto;

import java.util.Map;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/13 14:55
 */
public interface PayService {
    String prePay(OrderPayDto orderPayDto);

    String payNotify(Map<String, String> map);

    Boolean queryPayStatus(String orderNumber);
}
