package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.domain.Basket;
import com.powernode.domain.CartMoney;
import com.powernode.domain.ShopCart;
import com.powernode.service.BasketService;
import com.powernode.vo.CartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @PostMapping("changeItem")
    @ApiOperation("新增商品到购物车")
    public ResponseEntity<Void> addBasket(@RequestBody Basket basket) {
        basketService.addBasket(basket);
        return ResponseEntity.ok().build();
    }


    @GetMapping("info")
    @ApiOperation("查询当前用户的购物车详情")
    public ResponseEntity<CartVo> getBasketInfo() {
        // 获取当前用户id
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        CartVo cartVo = basketService.loadBasketInfo(userId);
        return ResponseEntity.ok(cartVo);
    }

    @PostMapping("totalPay")
    @ApiOperation("计算购物车价格")
    public ResponseEntity<CartMoney> getCartMoney(@RequestBody List<Long> basketIds){
        CartMoney cartMoney = basketService.calcuCartMoney(basketIds);
        return ResponseEntity.ok(cartMoney);
    }



}
