package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.domain.Order;
import com.powernode.dto.OrderConfirm;
import com.powernode.service.OrderService;
import com.powernode.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/9 14:02
 */
@RestController
@RequestMapping("p/myOrder")
@Api(tags = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("confirm")
    @ApiOperation("提交订单确认")
    public ResponseEntity<OrderVo> loadConfirm(@RequestBody OrderConfirm orderConfirm){
        OrderVo orderVo = orderService.toConfirm(orderConfirm);
        return ResponseEntity.ok(orderVo);
    }

    @PostMapping("submit")
    @ApiOperation("提交订单")
    public ResponseEntity<String> orderSubmit(@RequestBody OrderVo orderVo){
        String orderNum = orderService.orderSubmit(orderVo);
        return ResponseEntity.ok("order:" + orderNum);
    }

    @PostMapping("getOrder")
    @ApiOperation("根据单号查询订单")
    public Order getOrder(@RequestBody String orderNumber){
        return orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNumber,orderNumber));
    }

    @GetMapping("query")
    @ApiOperation("根据单号查询订单是否已经支付完成")
    public ResponseEntity<Boolean> queryOrderIsPay(@RequestParam("orderSn") String orderNumber){
        Boolean flag = orderService.queryOrderIsPay(orderNumber);
        return ResponseEntity.ok(flag);
    }

    @PostMapping("updateOrderStatus")
    @ApiOperation("修改订单状态")
    public void updateOrderStatus(@RequestBody String orderNumber){
        orderService.updateOrderStatus(orderNumber);
    }

}
