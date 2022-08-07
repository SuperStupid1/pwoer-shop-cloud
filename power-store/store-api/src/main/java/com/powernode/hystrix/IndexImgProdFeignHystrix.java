package com.powernode.hystrix;

import com.powernode.domain.Prod;
import com.powernode.feign.IndexImgProdFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 轮播图的远程调用的熔断
 * @author 杜波
 */
@Component
@Slf4j
public class IndexImgProdFeignHystrix implements IndexImgProdFeign {
    /**
     * 远程调用根据商品的id查询商品
     *
     * @param prodId
     * @return
     */
    @Override
    public Prod getProdById(Long prodId) {
        log.error("远程调用根据商品的id查询商品失败，商品id为{}", prodId);
        return null;
    }
}