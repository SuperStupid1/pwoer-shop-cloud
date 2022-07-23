package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.powernode.mapper.ProdMapper;
import com.powernode.service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.ProdCommMapper;
import com.powernode.domain.ProdComm;
import com.powernode.service.ProdCommService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Service
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService {

    @Autowired
    private ProdCommMapper prodCommMapper;

    @Autowired
    private ProdMapper prodMapper;

    @Override
    public Page<ProdComm> findProdCommPage(Page<ProdComm> page, ProdComm prodComm) {
        // 获取商品名
        String prodName = prodComm.getProdName();
        List<Long> prodIds = null;
        if (StringUtils.hasText(prodName)) {
            // 查询商品id
            List<Object> objects = prodMapper.selectObjs(new LambdaQueryWrapper<Prod>()
                    .select(Prod::getProdId)
                    .like(Prod::getProdName, prodName)
            );
            // 如果未查到将分页数据设置page数据为空返回
            if (!CollectionUtils.isEmpty(objects)) {
                page.setRecords(Collections.emptyList());
                page.setTotal(0L);
                return page;
            }
            // 将类型转化为Long
            prodIds = objects.stream()
                    .map(o -> Long.valueOf(o.toString()))
                    .collect(Collectors.toList());
        }
        // 查询评论列表
        Page<ProdComm> prodCommPage = prodCommMapper.selectPage(page, new LambdaQueryWrapper<ProdComm>()
                .eq(!ObjectUtils.isEmpty(prodComm.getStatus()), ProdComm::getStatus, prodComm.getStatus())
                .in(!CollectionUtils.isEmpty(prodIds),ProdComm::getProdId, prodIds)
                .orderByDesc(ProdComm::getRecTime)
        );
        // 获取评论查询到列表中的商品id列表
        List<ProdComm> records = prodCommPage.getRecords();
        if (CollectionUtils.isEmpty(records)){
            return prodCommPage;
        }
        List<Long> finalIds = records.stream()
                .map(ProdComm::getProdId)
                .collect(Collectors.toList());
        // 再查询商品列表
        List<Prod> prods = prodMapper.selectBatchIds(finalIds);
        // 将商品名封装到评论中
        records.forEach(pm->{
            Prod prod = prods.stream()
                    .filter(p -> p.getProdId().equals(pm.getProdId()))
                    .collect(Collectors.toList())
                    .get(0);
            pm.setProdName(prod.getProdName());
        });
        return prodCommPage;

    }
}
