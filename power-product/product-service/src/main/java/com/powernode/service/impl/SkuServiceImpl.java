package com.powernode.service.impl;

import com.powernode.domain.DbStockChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.SkuMapper;
import com.powernode.domain.Sku;
import com.powernode.service.SkuService;
/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService{

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public int changeStack(DbStockChange dbStockChange) {
        return skuMapper.updateStack(dbStockChange);
    }
}
