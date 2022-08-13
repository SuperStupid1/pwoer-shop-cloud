package com.powernode.service;

import com.powernode.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.dto.OrderConfirm;
import com.powernode.vo.OrderVo;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/9 9:44
 */
public interface OrderService extends IService<Order> {


    OrderVo toConfirm(OrderConfirm orderConfirm);

    String orderSubmit(OrderVo orderVo);

    Boolean queryOrderIsPay(String orderNumber);
}
