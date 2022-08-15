package com.powernode.controller;

import com.powernode.domain.OrderPayDto;
import com.powernode.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    /**
     * 需要放行
     * @return
     */
    @PostMapping("payNotify")
    @ApiOperation("第三方应用支付异步通知")
    public ResponseEntity<String> payNotify(@RequestParam Map<String,String> map){
        String result = payService.payNotify(map);
        return ResponseEntity.ok(result);
    }

    @PostMapping("p/order/queryPayStatus")
    @ApiOperation("查询支付结果")
    Boolean queryPayStatus(@RequestBody String orderNumber){
        return  payService.queryPayStatus(orderNumber);
    }
}
