package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdTagReference;
import com.powernode.domain.Sku;
import com.powernode.dto.DeliveryModeVo;
import com.powernode.service.ProdTagReferenceService;
import com.powernode.service.SkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.ProdMapper;
import com.powernode.domain.Prod;
import com.powernode.service.ProdService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Service
@Slf4j
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService{

    @Autowired
    private ProdMapper prodMapper;

    @Autowired
    private ProdTagReferenceService prodTagReferenceService;

    @Autowired
    private SkuService skuService;

    /**
     * 条件分页查询 商品
     * @param prodPage
     * @param prod
     * @return
     */
    @Override
    public Page<Prod> findProdPage(Page<Prod> prodPage, Prod prod) {
        return prodMapper.selectPage(prodPage, new LambdaQueryWrapper<Prod>()
                .eq(!ObjectUtils.isEmpty(prod.getStatus()),Prod::getStatus,prod.getStatus())
                .like(StringUtils.hasText(prod.getProdName()),Prod::getProdName,prod.getProdName())
                .orderByDesc(Prod::getCreateTime)
        );
    }

    /**
     * 新增商品
     * 1.给商品设置一些参数
     * 2.插入商品表  prodId
     * 3.处理sku表
     * 4.处理tag_referenc
     *
     * 事务的本质就是代理对象(aop实现)
     * spring
     * mysql
     * 事务失效的场景
     * 1.本地调用
     * 2.异常类型指定错误
     * 3.自己的异常被try catch
     * 4.数据库本身就不支持事务
     * 5.事务的传播特性指定级别错误
     * 6.没有开启事务功能
     * 7.jdk cglib 代理方式错误
     * 8.private修饰错误
     *
     * @param prod
     * @return
     */
    @Override
    public boolean saveProd(Prod prod) {
        log.info("新增商品{}", JSON.toJSONString(prod));
        prod.setShopId(1L);
        prod.setCreateTime(LocalDateTime.now());
        prod.setVersion(1);
        if (prod.getStatus().equals(1)) {
            prod.setPutawayTime(LocalDateTime.now());
        }
        DeliveryModeVo deliveryModeVo = prod.getDeliveryModeVo();
        String delivery = JSON.toJSONString(deliveryModeVo);
        prod.setDeliveryMode(delivery);
        // 插入数据库
        int i = prodMapper.insert(prod);

        if (i > 0) {
            // 处理sku
            this.handlerSku(prod.getProdId(), prod.getSkuList());
            // 处理tag
            this.handlerTag(prod.getProdId(), prod.getTagList());
        }
        return i > 0;


    }

    /**
     * 处理标签关系
     * @param prodId
     * @param tagList
     */
    private void handlerTag(Long prodId, List<Long> tagList) {
        if (CollectionUtils.isEmpty(tagList)) {
            return;
        }
        List<ProdTagReference> prodTagReferences = new ArrayList<>(tagList.size());
        tagList.forEach(tid -> {
            ProdTagReference prodTagReference = new ProdTagReference();
            prodTagReference.setShopId(1L);
            prodTagReference.setTagId(tid);
            prodTagReference.setProdId(prodId);
            prodTagReference.setCreateTime(LocalDateTime.now());
            prodTagReference.setStatus(true);
            prodTagReferences.add(prodTagReference);
        });
        prodTagReferenceService.saveBatch(prodTagReferences);

    }

    /**
     * 处理sku
     * @param prodId
     * @param skuList
     */
    private void handlerSku(Long prodId, List<Sku> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return;
        }
        skuList.forEach(sku -> {
            sku.setProdId(prodId);
            sku.setRecTime(LocalDateTime.now());
            sku.setUpdateTime(LocalDateTime.now());
            sku.setVersion(0);
            sku.setActualStocks(sku.getStocks());
            sku.setIsDelete(0);
            sku.setStatus(1);
        });
        skuService.saveBatch(skuList);
    }
}
