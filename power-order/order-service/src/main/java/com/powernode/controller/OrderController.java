package com.powernode.controller;

import com.powernode.dto.OrderConfirm;
import com.powernode.service.OrderService;
import com.powernode.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
}
