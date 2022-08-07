package com.powernode.feign;

import com.powernode.domain.Prod;
import com.powernode.hystrix.IndexImgProdFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/3 16:40
 */
@FeignClient(value = "product-service",fallback = IndexImgProdFeignHystrix.class)
public interface IndexImgProdFeign {
    /**
     * 远程调用根据商品的id查询商品
     *
     * @param prodId
     * @return
     */
    @GetMapping("prod/prod/getProdById")
    Prod getProdById(@RequestParam Long prodId);
}
