package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.powernode.domain.*;
import com.powernode.feign.CartProdFeign;
import com.powernode.vo.CartVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.BasketMapper;
import com.powernode.service.BasketService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/6 15:40
 */
@Service
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService {

    @Autowired
    private BasketMapper basketMapper;

    @Autowired
    private CartProdFeign cartProdFeign;

    /**
     * 查询当前用户购物车数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer findUserBasketCount(String userId) {
        List<Object> obj = basketMapper.selectObjs(new QueryWrapper<Basket>()
                .select(" IFNULL(sum(basket_count),0) ")
                .eq("user_id", userId)
        );
        if (!CollectionUtils.isEmpty(obj)) {
            Object o = obj.get(0);
            return Integer.parseInt(o.toString());
        }
        return 0;

    }


    /**
     * 新增购物车
     * @param basket
     */
    @Override
    public void addBasket(Basket basket) {
        // 查询购物车中是否已经存在该规格的商品
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        basket.setUserId(userId);
        basket.setBasketDate(LocalDateTime.now());
        Basket old = basketMapper.selectOne(new LambdaQueryWrapper<Basket>()
                .eq(Basket::getSkuId, basket.getSkuId())
                .eq(Basket::getUserId, userId)
        );
        if (ObjectUtils.isEmpty(old)) {
            // 如果不存在那么就插入数据
            basketMapper.insert(basket);
            return;
        }

        // 如果存在那么就修改数量
        int newBasketCount = old.getBasketCount() + basket.getBasketCount();
        // 判断总数量是否大于0
        if (newBasketCount < 1) {
            // 数量为0删除该商品
            basketMapper.deleteById(old.getBasketId());
            return;
        }

        if (newBasketCount < 0) {
            throw new IllegalArgumentException("购物车中商品的数量不能小于0");
        }
        if (newBasketCount > 99) {
            // 限制购物数量每件商品只能添加99件
            throw new IllegalArgumentException("购物车中商品的数量不能超过99,只能添加再添加" + (99 - old.getBasketCount()) + "件");
        }
        old.setBasketCount(newBasketCount);
        basketMapper.updateById(old);
    }

    /**
     * 查询当前用户的购物车信息
     *
     * @param userId
     * @return
     */
    @Override
    public CartVo loadBasketInfo(String userId) {
        CartVo cartVo = new CartVo();
        // 查询当前用户对应的购物车列表
        List<Basket> basketList = basketMapper.selectList(new LambdaQueryWrapper<Basket>()
                .eq(Basket::getUserId, userId));
        if (CollectionUtils.isEmpty(basketList)) {
            return cartVo;
        }

        // 查询当前用户对应的购物车列表对应的店铺
        Set<Long> shopIds = basketList.stream().map(Basket::getShopId).collect(Collectors.toSet());

        shopIds.forEach(shopId -> {
            ShopCart shopCart = new ShopCart();
            ArrayList<CartItem> cartItemList = new ArrayList<>();
            // 查询当前店铺对应的basket集合
            List<Basket> baskets = basketList.stream()
                    .filter(basket -> basket.getShopId().equals(shopId))
                    .collect(Collectors.toList());
            // 查询对应店铺的sku列表
            List<Long> skuIds = baskets.stream().map(Basket::getSkuId).collect(Collectors.toList());
            List<Sku> skuList = cartProdFeign.getSkuByIds(skuIds);
            Map<Long, Sku> skuMap = skuList.stream().collect(Collectors.toMap(Sku::getSkuId, sku -> sku));
            // 封装结果
            baskets.forEach(basket -> {
                // 封装对应的basket属性
                CartItem cartItem = new CartItem();
                BeanUtils.copyProperties(basket, cartItem);
                // 封装对应的sku属性
                Sku sku = skuMap.get(basket.getSkuId());
                BeanUtils.copyProperties(sku, cartItem);
                cartItem.setChecked(true);
                cartItemList.add(cartItem);
            });
            shopCart.setShopCartItems(cartItemList);
            cartVo.getShopCarts().add(shopCart);
        });

        return cartVo;
    }



    /**
     * 计算购物车中商品的价格
     * @param basketIds
     * @return
     */
    @Override
    public CartMoney calcuCartMoney(List<Long> basketIds) {
        CartMoney cartMoney = new CartMoney();
        if (CollectionUtils.isEmpty(basketIds)){
            return new CartMoney();
        }
        List<Basket> baskets = basketMapper.selectList(new LambdaQueryWrapper<Basket>()
                .in(Basket::getBasketId, basketIds)
        );
        // 查询对应的sku信息中的价格
        List<Long> skuIds = baskets.stream().map(Basket::getSkuId).collect(Collectors.toList());
        List<Sku> skuList = cartProdFeign.getSkuByIds(skuIds);
        Map<Long, BigDecimal> skuPriceMap = skuList.stream().collect(Collectors.toMap(Sku::getSkuId, Sku::getPrice));
        // 遍历计算总价格
        ArrayList<BigDecimal> basketMoneyList = new ArrayList<>();
        baskets.forEach(basket -> {
            Integer basketCount = basket.getBasketCount();
            BigDecimal price = skuPriceMap.get(basket.getSkuId());
            BigDecimal oneBasketMoney = BigDecimal.valueOf(basketCount).multiply(price);
            basketMoneyList.add(oneBasketMoney);
        });
        BigDecimal totalMoney = basketMoneyList.stream().reduce(BigDecimal::add).get();
        cartMoney.setTotalMoney(totalMoney);
        // 判断总金额是否满足免运费（满90）
        if (totalMoney.intValue() < 99){
            // 不满99元收6元运费
            cartMoney.setTransMoney(new BigDecimal(6));
        }
        // 计算最终金额 总金额 + 运费 - 优惠金额
        cartMoney.setFinalMoney(cartMoney.getTotalMoney().add(cartMoney.getTransMoney()).subtract(cartMoney.getSubtractMoney()));

        return cartMoney;
    }
}
