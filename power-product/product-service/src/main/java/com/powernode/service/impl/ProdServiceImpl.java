package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdComm;
import com.powernode.domain.ProdTagReference;
import com.powernode.domain.Sku;
import com.powernode.dto.DeliveryModeVo;
import com.powernode.service.ProdCommService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Service
@Slf4j
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {

    @Autowired
    private ProdMapper prodMapper;

    @Autowired
    private ProdTagReferenceService prodTagReferenceService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private ProdCommService prodCommService;

    /**
     * 条件分页查询 商品
     *
     * @param prodPage
     * @param prod
     * @return
     */
    @Override
    public Page<Prod> findProdPage(Page<Prod> prodPage, Prod prod) {
        return prodMapper.selectPage(prodPage, new LambdaQueryWrapper<Prod>()
                .eq(!ObjectUtils.isEmpty(prod.getStatus()), Prod::getStatus, prod.getStatus())
                .like(StringUtils.hasText(prod.getProdName()), Prod::getProdName, prod.getProdName())
                .orderByDesc(Prod::getCreateTime)
        );
    }

    /**
     * 新增商品
     * 1.给商品设置一些参数
     * 2.插入商品表  prodId
     * 3.处理sku表
     * 4.处理tag_referenc
     * <p>
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
     *
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
     *
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

    /**
     * 查询指定期间内总条数
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public Integer getTotalCount(LocalDateTime start, LocalDateTime end) {
        return prodMapper.selectCount(new LambdaQueryWrapper<Prod>()
                .between(start != null && end != null, Prod::getUpdateTime, start, end)
        );
    }

    /**
     * 分页查询prod数据 组装一些属性
     * 1.分页查询商品集合
     * 2.标签分组
     * 2.好评率
     *
     * @param page
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<Prod> findProdPage2Es(Page<Prod> page, LocalDateTime start, LocalDateTime end) {
        Page<Prod> prodPage = prodMapper.selectPage(page, new LambdaQueryWrapper<Prod>()
                .between(!ObjectUtils.isEmpty(start) && !ObjectUtils.isEmpty(end), Prod::getUpdateTime, start, end)
        );
        // 获取商品集合
        List<Prod> prodList = prodPage.getRecords();
        if (CollectionUtils.isEmpty(prodList)) {
            return prodList;
        }
        // 1.处理标签  把一个商品对应的标签的集合查到
        List<Long> prodIdList = prodList.stream()
                .map(Prod::getProdId)
                .collect(Collectors.toList());

        // 查询tag_reference
        List<ProdTagReference> prodTagReferenceList = prodTagReferenceService.list(new LambdaQueryWrapper<ProdTagReference>()
                .in(ProdTagReference::getProdId, prodIdList)
        );
        // 循环商品 给每个商品都填充tagList属性
        prodList.forEach(prod -> {
            List<Long> tagIdList = prodTagReferenceList.stream()
                    .filter(prodTagReference -> prodTagReference.getProdId().equals(prod.getProdId()))
                    .map(ProdTagReference::getTagId)
                    .collect(Collectors.toList());
            prod.setTagList(tagIdList);
        });
        // 2.处理评论 好评数   好评率
        // 查评论
        List<ProdComm> prodCommList = prodCommService.list(new LambdaQueryWrapper<ProdComm>()
                .in(ProdComm::getProdId, prodIdList)
        );
        prodList.forEach(prod -> {
            List<ProdComm> commList = prodCommList.stream()
                    .filter(prodComm -> prodComm.getProdId().equals(prod.getProdId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(commList)){
                int totalComm = commList.size();
                long goodsCount = commList.stream()
                        .filter(prodComm -> prodComm.getEvaluate().equals(0))
                        .count();

                BigDecimal goodsLv = new BigDecimal(goodsCount)
                        .divide(new BigDecimal(totalComm),2,BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100));
                prod.setPraiseNumber(goodsCount);
                prod.setPositiveRating(goodsLv);
            }
        });

        return prodList;
    }
}
