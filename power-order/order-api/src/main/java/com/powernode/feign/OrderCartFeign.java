package com.powernode.feign;

import com.powernode.domain.Basket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/9 15:13
 */
@FeignClient(value = "cart-service")
public interface OrderCartFeign {

    @PostMapping("p/shopCart/getBasketById")
    List<Basket> getBasketById(@RequestBody List<Long> basketIds);

    @PostMapping("p/shopCart/clearCart/{userId}")
    void clearCart(@RequestBody List<Long> skuIds, @PathVariable String userId);
}
