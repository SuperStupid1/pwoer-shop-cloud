package com.powernode.controller;

import com.powernode.service.BasketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车管理
 *
 * @author DuBo
 * @createDate 2022/8/7 11:45
 */
@RestController
@RequestMapping("p/shopCart")
@Api(tags = "购物车管理")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping("prodCount")
    @ApiOperation("查询当前用户的购物车数量")
    public ResponseEntity<Integer> getBasketCount() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Integer count = basketService.findUserBasketCount(userId);
        return ResponseEntity.ok(count);
    }
}
