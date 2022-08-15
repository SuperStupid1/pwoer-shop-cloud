package com.powernode.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/15 14:36
 */
@FeignClient(value = "pay-service")
public interface OrderPayFeign {

    @PostMapping("p/order/queryPayStatus")
    Boolean queryPayStatus(@RequestBody String orderNumber);
}
