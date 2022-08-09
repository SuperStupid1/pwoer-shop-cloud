package com.powernode.feign;

import com.powernode.domain.UserAddr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/9 14:23
 */
@FeignClient(value = "member-service")
public interface OrderMemberFeign {

    /**
     * 根据用户名查询默认地址
     * @param userId
     * @return
     */
    @PostMapping("p/address/getDefaultAddr")
    UserAddr getDefaultAddrByUserId(@RequestBody String userId);
}
