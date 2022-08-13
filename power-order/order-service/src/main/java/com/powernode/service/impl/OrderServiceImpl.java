package com.powernode.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.constant.OrderConstant;
import com.powernode.constant.QueueConstant;
import com.powernode.domain.*;
import com.powernode.dto.OrderConfirm;
import com.powernode.dto.StockChangeDto;
import com.powernode.feign.OrderCartFeign;
import com.powernode.feign.OrderMemberFeign;
import com.powernode.feign.OrderProdFeign;
import com.powernode.service.OrderItemService;
import com.powernode.vo.OrderVo;
import com.powernode.vo.ShopOrder;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.OrderMapper;
import com.powernode.service.OrderService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemService orderItemService;


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
     *
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
        List<BigDecimal> totalMoneyList = new ArrayList<>(basketList.size());
        // 定义一个集合用于保存订单商品总数量
        List<Integer> totalCountList = new ArrayList<>(basketList.size());
        // 遍历
        basketMap.forEach((shopId, baskets) -> {
            // 创建店铺订单对象
            ShopOrder shopOrder = new ShopOrder();
            List<OrderItem> orderItemList = new ArrayList<>(baskets.size());
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
                BeanUtils.copyProperties(basket, orderItem);
                Sku sku = skuMap.get(basket.getSkuId());
                BeanUtils.copyProperties(sku, orderItem);
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
     *
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


    /**
     * 订单提交
     *
     * @return
     */
    @Override
    public String orderSubmit(OrderVo orderVo) {

        // 1、生成订单号
        Snowflake snowflake = new Snowflake(0, 0);
        String orderSn = snowflake.nextIdStr();
        // 2、清空购物车
        List<OrderItem> orderItemList = clearBasket(orderVo);
        // 3、扣减prod表和sku表的库存
        StockChangeDto stockChangeDto = deductDbStock(orderItemList);
        // 4、扣减es的库存
        changEsStock(stockChangeDto);
        // 5、生成订单信息
        Order order = creatOrder(orderVo, orderSn, orderItemList);
        // 6、超时订单处理
        overtimeOrderHandle(stockChangeDto,orderSn);
        // 7、定时查询(延迟队列)用户是否支付，如未支付进行微信公众号通知
        sendWxMsg(order.getUserId(),orderSn,order.getProductNums(),order.getActualTotal());
        return orderSn;
    }


    /**
     * 监听订单超时未支付的死信队列，回滚库存
     * @param message
     * @param channel
     */
    @RabbitListener(queues = OrderConstant.ORDER_TIMEOUT_DEAD_QUEUE,concurrency = "3-5")
    public void restoreDbStock(Message message, Channel channel){
        String str = new String(message.getBody());
        OvertimeOrderDto overtimeOrderDto = JSON.parseObject(str, OvertimeOrderDto.class);
        // 回滚库存
        StockChangeDto stockChangeDto = overtimeOrderDto.getStockChangeDto();
        // 对数量乘以-1
        List<DbStockChange> prodChanges = stockChangeDto.getProdChanges();
        prodChanges.forEach(dbStockChange -> dbStockChange.setCount(dbStockChange.getCount() * -1));
        List<DbStockChange> skuChanges = stockChangeDto.getSkuChanges();
        skuChanges.forEach(dbStockChange -> dbStockChange.setCount(dbStockChange.getCount() * -1));
        stockChangeDto.setProdChanges(prodChanges);
        stockChangeDto.setSkuChanges(skuChanges);
        // 远程调用修改库存
        orderProdFeign.changeStack(stockChangeDto);
        // 修改订单状态
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNumber, overtimeOrderDto.getOrderSn())
        );
        order.setStatus(6);
        order.setDeleteStatus(1);
        orderMapper.updateById(order);
        // 删除orderItem todo...


        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 订单支付微信公众号通知
     * @param userId
     * @param orderSn
     * @param productNums
     * @param total
     */
    private void sendWxMsg(String userId, String orderSn, Integer productNums, BigDecimal total) {
        WxMsgDto wxMsgDto = new WxMsgDto(userId, orderSn, productNums, total);
        rabbitTemplate.convertAndSend(QueueConstant.WX_MSG_DELAY_QUEUE,JSON.toJSONString(wxMsgDto));
    }

    /**
     * 超时未支付订单处理，发送消息到延迟队列 回溯库存,修改订单状态将原来的扣减数量乘以-1
     * @param stockChangeDto
     * @param orderSn
     */
    private void overtimeOrderHandle(StockChangeDto stockChangeDto, String orderSn) {
        OvertimeOrderDto overtimeOrderDto = new OvertimeOrderDto(stockChangeDto, orderSn);
        rabbitTemplate.convertAndSend(QueueConstant.ORDER_OVERTIME_DELAY_QUEUE,JSON.toJSONString(overtimeOrderDto));
    }

    /**
     * 生成订单和orderItem信息
     * @param orderVo
     * @param orderSn
     * @param orderItemList
     */
    private Order creatOrder(OrderVo orderVo, String orderSn, List<OrderItem> orderItemList) {
        // 封装order信息
        Order order = new Order();
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        order.setUserId(userId);
        // 获取所有商品名字（去重）,并用逗号拼接
        Set<String> prodNames = orderItemList.stream().map(OrderItem::getProdName).collect(Collectors.toSet());
        StringBuilder sb = new StringBuilder();
        prodNames.forEach(prodName->sb.append(prodName).append(","));
        sb.replace(sb.length()-1,sb.length(),"");
        order.setProdName(sb.toString());
        order.setOrderNumber(orderSn);
        order.setTotal(orderVo.getTotal());
        order.setActualTotal(orderVo.getActualTotal());
        // 由于微信支付需要企业认证，所以本项目接入支付宝支付
        order.setPayType(2);
        order.setRemarks(orderVo.getRemarks());
        order.setFreightAmount(orderVo.getTransfee());
        order.setAddrOrderId(orderVo.getUserAddr().getAddrId());
        order.setProductNums(orderVo.getTotalCount());
        order.setCreateTime(LocalDateTime.now());
        order.setIsPayed(false);
        // 保存订单信息
        int row = orderMapper.insert(order);
        if (row > 0){
            // 封装每条orderItem信息
            List<Long> skuIds = orderItemList.stream().map(OrderItem::getSkuId).collect(Collectors.toList());
            Map<Long, String> skuMap = orderProdFeign.getSkuByIds(skuIds).stream()
                    .collect(Collectors.toMap(Sku::getSkuId, Sku::getSkuName));
            orderItemList.forEach(orderItem -> {
                orderItem.setCommSts(0);
                orderItem.setOrderNumber(orderSn);
                orderItem.setSkuName(skuMap.get(orderItem.getSkuId()));
                orderItem.setRecTime(LocalDateTime.now());
            });
            orderItemService.saveBatch(orderItemList);
        }

        return order;
    }

    /**
     * 扣减es的库存
     * @param stockChangeDto
     */
    private void changEsStock(StockChangeDto stockChangeDto) {
        List<EsChange> esChanges = new ArrayList<>();
        stockChangeDto.getProdChanges().forEach(dbStockChange ->{
            EsChange esChange = EsChange.builder().prodId(dbStockChange.getId()).count(dbStockChange.getCount()).build();
            esChanges.add(esChange);
        });
        // 发送消息到消息队列
        rabbitTemplate.convertAndSend(QueueConstant.ES_CHANGE_QUEUE, JSON.toJSONString(esChanges));
    }


    /**
     * 扣减库存
     *
     * @param orderItemList
     */
    private StockChangeDto deductDbStock(List<OrderItem> orderItemList) {
        List<DbStockChange> skuChanges = new ArrayList<>();
        List<DbStockChange> prodChanges = new ArrayList<>();

        // 获取每个sku需要修改的库存量(方便取消订单回库存 采用带符号的库存)
        orderItemList.forEach(orderItem -> skuChanges.add(new DbStockChange(orderItem.getSkuId(), -1 * orderItem.getProdCount())));

        // 获取每种商品需要修改的库存量（方便取消订单回库存 采用带符号的库存)
        Map<Long, List<OrderItem>> prodIdMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItem::getProdId));
        prodIdMap.forEach((prodId, orderItems) -> {
            Integer prodCountSum = orderItems.stream().map(OrderItem::getProdCount).reduce(Integer::sum).get();
            prodChanges.add(new DbStockChange(prodId, -1 * prodCountSum));
        });

        StockChangeDto stockChangeDto = new StockChangeDto(skuChanges, prodChanges);
        // 远程调用商品服务修改库存
        orderProdFeign.changeStack(stockChangeDto);
        return stockChangeDto;
    }

    /**
     * 清空购物车
     *
     * @param orderVo
     */
    private List<OrderItem> clearBasket(OrderVo orderVo) {
        // 获取所有商品条目
        List<OrderItem> orderItemList = new ArrayList<>();
        orderVo.getShopCartOrders().stream()
                .map(ShopOrder::getShopCartItemDiscounts)
                .forEach(orderItemList::addAll);
        // 获取所有的skuId
        List<Long> skuIds = orderItemList.stream()
                .map(OrderItem::getSkuId)
                .collect(Collectors.toList());
        // 获取当前登录用户
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        // 远程调用删除购物车
        orderCartFeign.clearCart(skuIds, userId);
        return orderItemList;
    }
}

