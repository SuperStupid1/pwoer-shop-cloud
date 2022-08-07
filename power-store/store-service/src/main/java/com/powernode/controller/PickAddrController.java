package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.PickAddr;
import com.powernode.service.PickAddrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/2 19:28
 */
@RestController
@RequestMapping("shop/pickAddr")
@Api(tags = "自提点管理接口")
public class PickAddrController {

    @Autowired
    private PickAddrService pickAddrService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('shop:pickAddr:page')")
    @ApiOperation("查询自提点")
    public ResponseEntity<Page<PickAddr>> loadPickAddr(Page<PickAddr> page,PickAddr pickAddr){
        Page<PickAddr> pickAddrPage = pickAddrService.page(page,new LambdaQueryWrapper<PickAddr>()
                .like(!ObjectUtils.isEmpty(pickAddr.getAddrName()),PickAddr::getAddrName,pickAddr.getAddrName())
        );
        return ResponseEntity.ok(page);
    }

}
