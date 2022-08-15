package com.powernode.feign;

import com.powernode.domain.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/13 15:52
 */
@FeignClient(value = "order-service")
public interface PayOrderFeign {


    @PostMapping("p/myOrder/getOrder")
    Order getOrder(@RequestBody String orderNumber);

    @PostMapping("p/myOrder/updateOrderStatus")
    void updateOrderStatus(@RequestBody String orderNumber);
}
