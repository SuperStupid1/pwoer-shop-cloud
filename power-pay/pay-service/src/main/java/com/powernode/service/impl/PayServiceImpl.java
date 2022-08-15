package com.powernode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.powernode.domain.Order;
import com.powernode.domain.OrderPayDto;
import com.powernode.feign.PayOrderFeign;
import com.powernode.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/13 14:58
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {


    @Value("${alipay.public-key}")
    private String publicKey;

    @Value("${alipay.private-key}")
    private String privateKey;

    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    @Value("${alipay.charset}")
    private String charset;

    @Value("${alipay.sign-type}")
    private String signType;

    @Value("${alipay.app-id}")
    private String appId;

    @Value("${alipay.format}")
    private String format;

    @Value("${alipay.server-url}")
    private String serverUrl;

    @Autowired
    private PayOrderFeign payOrderFeign;

    /**
     *
     *  调用第三方支付平台生成二维码
     *  需要远程调用 查询订单的相关信息 金额 描述等
     *  组装一个支付的对象
     *  调用ali的统一下单
     * @param orderPayDto
     * @return
     */
    @Override
    public String prePay(OrderPayDto orderPayDto) {
        String orderNumber = orderPayDto.getOrderNumber();
        if (StringUtils.isEmpty(orderNumber)){
            log.error("订单号为空");
            throw new IllegalArgumentException("统记一下单订单号为空");
        }
        // 远程调用查询订单信息
        Order order = payOrderFeign.getOrder(orderNumber);
        if (ObjectUtils.isEmpty(order)){
            log.error("订单不存在");
            throw new RuntimeException("服务器维护中");
        }

        switch (orderPayDto.getPayType()){
            // 微信支付 todo
            case 1:
                break;
                // 支付宝
            case 2:
                AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format,
                        charset, alipayPublicKey, signType);
                AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
                request.setNotifyUrl("http://y82t65.natappfree.cc/p/order/payNotify");
                JSONObject bizContent = new JSONObject();
                bizContent.put("out_trade_no", order.getOrderNumber());
                bizContent.put("total_amount", order.getActualTotal());
                bizContent.put("subject", order.getProdName());
                request.setBizContent(bizContent.toString());
                AlipayTradePrecreateResponse response = null;
                try {
                    response = alipayClient.execute(request);
                } catch (AlipayApiException e) {
                    throw new RuntimeException(e);
                }
                if(response.isSuccess()){
                    return response.getQrCode();
                } else {
                    System.out.println("调用失败");
                    return null;
                }
            default:
                break;
        }
        return null;
    }

    /**
     * 获取支付宝支付通知
     * @param map
     * @return
     */
    @Override
    public String payNotify(Map<String, String> map) {
        // 验签
        boolean verifySignResult = false;
        try {
            verifySignResult = AlipaySignature.verifyV1(map, alipayPublicKey, charset, signType);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        if (verifySignResult){
            // 获取支付结果
            String tradeStatus = map.get("trade_status");
            if (tradeStatus.equals("TRADE_SUCCESS")){
                // 支付成功修改订单状态
                // 获取订单号
                String orderNumber = map.get("out_trade_no");
                payOrderFeign.updateOrderStatus(orderNumber);
                return "success";
            }
        }
        return null;
    }

    /**
     * 查询支付结果
     * @param orderNumber
     * @return
     */
    @Override
    public Boolean queryPayStatus(String orderNumber) {

        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format,
                charset, alipayPublicKey, signType);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "  \"out_trade_no\":\" "+orderNumber+" \"," +
                "}");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        if(response.isSuccess()){
            return (response.getTradeStatus().equals("TRADE_SUCCESS"));
        } else {
            System.out.println("调用失败");
        }
        return false;
    }
}
