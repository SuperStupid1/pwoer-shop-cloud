package com.powernode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
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
                request.setNotifyUrl("");
                JSONObject bizContent = new JSONObject();
                bizContent.put("out_trade_no", order.getOrderNumber());
                bizContent.put("total_amount", order.getActualTotal());
                bizContent.put("subject", order.getProdName());

                //// 商品明细信息，按需传入
                //JSONArray goodsDetail = new JSONArray();
                //JSONObject goods1 = new JSONObject();
                //goods1.put("goods_id", "goodsNo1");
                //goods1.put("goods_name", "子商品1");
                //goods1.put("quantity", 1);
                //goods1.put("price", 0.01);
                //goodsDetail.add(goods1);
                //bizContent.put("goods_detail", goodsDetail);

                //// 扩展信息，按需传入
                //JSONObject extendParams = new JSONObject();
                //extendParams.put("sys_service_provider_id", "2088511833207846");
                //bizContent.put("extend_params", extendParams);

                //// 结算信息，按需传入
                //JSONObject settleInfo = new JSONObject();
                //JSONArray settleDetailInfos = new JSONArray();
                //JSONObject settleDetail = new JSONObject();
                //settleDetail.put("trans_in_type", "defaultSettle");
                //settleDetail.put("amount", 0.01);
                //settleDetailInfos.add(settleDetail);
                //settleInfo.put("settle_detail_infos", settleDetailInfos);
                //bizContent.put("settle_info", settleInfo);

                //// 二级商户信息，按需传入
                //JSONObject subMerchant = new JSONObject();
                //subMerchant.put("merchant_id", "2088000603999128");
                //bizContent.put("sub_merchant", subMerchant);

                //// 业务参数信息，按需传入
                //JSONObject businessParams = new JSONObject();
                //businessParams.put("busi_params_key", "busiParamsValue");
                //bizContent.put("business_params", businessParams);

                //// 营销信息，按需传入
                //JSONObject promoParams = new JSONObject();
                //promoParams.put("promo_params_key", "promoParamsValue");
                //bizContent.put("promo_params", promoParams);

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
}
