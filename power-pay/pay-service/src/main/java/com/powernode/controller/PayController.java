package com.powernode.controller;

import com.powernode.domain.OrderPayDto;
import com.powernode.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/13 14:36
 */
@RestController
@RequestMapping("p/order")
@Api(tags = "支付接口")
public class PayController {

    @Autowired
    private PayService payService;

    @PostMapping("pay")
    @ApiOperation("生成支付二维码")
    public ResponseEntity<String> orderPay(@RequestBody OrderPayDto orderPayDto){
        String qrCode = payService.prePay(orderPayDto);
        return ResponseEntity.ok(qrCode);
    }
}
