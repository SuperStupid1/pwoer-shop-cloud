package com.powernode.service.impl;

import com.powernode.domain.*;
import com.powernode.dto.OrderConfirm;
import com.powernode.feign.OrderCartFeign;
import com.powernode.feign.OrderMemberFeign;
import com.powernode.feign.OrderProdFeign;
import com.powernode.vo.OrderVo;
import com.powernode.vo.ShopOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.OrderMapper;
import com.powernode.service.OrderService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/9 9:44
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMemberFeign orderMemberFeign;

    @Autowired
    private OrderProdFeign orderProdFeign;

    @Autowired
    private OrderCartFeign orderCartFeign;


    /**
     * 商品下订单结算
     *
     * @param orderConfirm
     * @return
     */
    @Override
    public OrderVo toConfirm(OrderConfirm orderConfirm) {
        OrderVo orderVo = new OrderVo();
        // 1.查询用户的收货地址（默认地址）
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserAddr userAddr = orderMemberFeign.getDefaultAddrByUserId(userId);
        if (ObjectUtils.isEmpty(userAddr)) {
            throw new RuntimeException("用户：" + userId + "没有收货地址，请先添加收货地址");
        }
        orderVo.setUserAddr(userAddr);
        // 2.判断是从商品详情页面直接购买 还是从购物车结算购买（商品页面购买不会传basketIds）
        if (CollectionUtils.isEmpty(orderConfirm.getBasketIds())) {
            // 商品详情页面购买
            prodToConfirm(orderConfirm, orderVo);
        } else {
            // 购物车结算购买
            cartToConfirm(orderConfirm, orderVo);
        }
        return orderVo;
    }

    /**
     * 购物车页面下订单
     * @param orderConfirm
     * @param orderVo
     */
    private void cartToConfirm(OrderConfirm orderConfirm, OrderVo orderVo) {
        List<Long> basketIds = orderConfirm.getBasketIds();
        // 查询对应的basket信息
        List<Basket> basketList = orderCartFeign.getBasketById(basketIds);
        if (CollectionUtils.isEmpty(basketList)) {
            throw new RuntimeException("服务器维护中");
        }
        // 按店铺分组
        Map<Long, List<Basket>> basketMap = basketList.stream().collect(Collectors.groupingBy(Basket::getShopId));
        List<ShopOrder> shopOrders = new ArrayList<>();
        // 定义一个集合用于保存订单商品总金额
        List<BigDecimal> totalMoneyList = new ArrayList<>();
        // 定义一个集合用于保存订单商品总数量
        List<Integer> totalCountList = new ArrayList<>();
        // 遍历
        basketMap.forEach((shopId,baskets) ->{
            // 创建店铺订单对象
            ShopOrder shopOrder = new ShopOrder();
            List<OrderItem>  orderItemList = new ArrayList<>();
            List<Long> skuIds = baskets.stream()
                    .map(Basket::getSkuId)
                    .collect(Collectors.toList());
            // 查询对应的商品sku信息
            List<Sku> skuList = orderProdFeign.getSkuByIds(skuIds);
            Map<Long, Sku> skuMap = skuList.stream().collect(Collectors.toMap(Sku::getSkuId, sku -> sku));
            // 遍历当前店铺的basket集合
            baskets.forEach(basket -> {
                // 创建商品条目对象
                OrderItem orderItem = new OrderItem();
                // 拷贝属性
                BeanUtils.copyProperties(basket,orderItem);
                Sku sku = skuMap.get(basket.getSkuId());
                BeanUtils.copyProperties(sku,orderItem);
                // 设置购物车产品个数
                Integer basketCount = basket.getBasketCount();
                orderItem.setProdCount(basketCount);
                totalCountList.add(basketCount);
                orderItem.setRecTime(LocalDateTime.now());
                // 设置商品总金额
                BigDecimal productTotalAmount = sku.getPrice().multiply(new BigDecimal(basketCount));
                totalMoneyList.add(productTotalAmount);
                orderItem.setProductTotalAmount(productTotalAmount);
                orderItemList.add(orderItem);
            });
            shopOrder.setShopCartItemDiscounts(orderItemList);
            shopOrders.add(shopOrder);
        });
        // 设置店铺集合
        orderVo.setShopCartOrders(shopOrders);
        // 设置总订单总数量
        Integer totalCount = totalCountList.stream().reduce(Integer::sum).get();
        orderVo.setTotalCount(totalCount);
        // 设置总实际金额
        BigDecimal total = totalMoneyList.stream().reduce(BigDecimal::add).get();
        orderVo.setTotal(total);
        // 计算运费(不满99 6云运费)
        if (total.intValue() < 99) {
            orderVo.setTransfee(new BigDecimal(6));
        }
        // 计算总金额
        BigDecimal actualTotal = total.add(orderVo.getTransfee()).subtract(orderVo.getShopReduce());
        orderVo.setActualTotal(actualTotal);
    }


    /**
     * 商品详情页下订单
     * @param orderConfirm
     * @param orderVo
     */
    private void prodToConfirm(OrderConfirm orderConfirm, OrderVo orderVo) {
        OrderItem orderItem = orderConfirm.getOrderItem();

        Long skuId = orderItem.getSkuId();
        // 查询对应的sku信息
        List<Sku> skuList = orderProdFeign.getSkuByIds(Arrays.asList(skuId));
        if (CollectionUtils.isEmpty(skuList)) {
            throw new RuntimeException("服务器维护中");
        }
        Sku sku = skuList.get(0);
        // 获取单价
        BigDecimal price = sku.getPrice();
        // 获取商品数量
        Integer prodCount = orderItem.getProdCount();
        orderVo.setTotalCount(prodCount);
        // 计算实际金额
        BigDecimal total = price.multiply(new BigDecimal(prodCount));
        orderVo.setTotal(total);
        // 计算运费(不满99 6云运费)
        if (total.intValue() < 99) {
            orderVo.setTransfee(new BigDecimal(6));
        }
        // 计算总金额
        BigDecimal actualTotal = total.add(orderVo.getTransfee()).subtract(orderVo.getShopReduce());
        orderVo.setActualTotal(actualTotal);
        // 创建店铺集合
        List<ShopOrder> shopOrders = new ArrayList<>();
        ShopOrder shopOrder = new ShopOrder();
        // 创建商品条目集合
        List<OrderItem> orderItemList = new ArrayList<>();
        // 拷贝属性
        BeanUtils.copyProperties(sku, orderItem);
        orderItem.setProductTotalAmount(actualTotal);
        orderItemList.add(orderItem);
        shopOrder.setShopCartItemDiscounts(orderItemList);
        shopOrders.add(shopOrder);
        orderVo.setShopCartOrders(shopOrders);
    }
}
