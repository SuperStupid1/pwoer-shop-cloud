package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdPropValue;
import com.powernode.service.ProdPropValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.ProdPropMapper;
import com.powernode.domain.ProdProp;
import com.powernode.service.ProdPropService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Service
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService {

    @Autowired
    private ProdPropMapper prodPropMapper;

    @Autowired
    private ProdPropValueService prodPropValueService;

    @Override
    public Page<ProdProp> findProdPropPage(Page<ProdProp> page, ProdProp prodProp) {

        Page<ProdProp> prodPropPage = prodPropMapper.selectPage(page, new LambdaQueryWrapper<ProdProp>()
                .like(StringUtils.hasText(prodProp.getPropName()), ProdProp::getPropName, prodProp.getPropName())
        );

        List<ProdProp> prodProps = prodPropPage.getRecords();
        if (CollectionUtils.isEmpty(prodProps)) {
            return prodPropPage;
        }
        // 查询属性id对应的属性值进行封装
        prodProps.forEach(p -> {
            List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>()
                    .eq(ProdPropValue::getPropId, p.getPropId()));
            p.setProdPropValues(list);
        });

        return prodPropPage;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveProdProp(ProdProp prodProp) {
        prodProp.setRule(1);
        prodProp.setShopId(1L);
        int row = prodPropMapper.insert(prodProp);
        List<ProdPropValue> prodPropValues = prodProp.getProdPropValues();
        List<ProdPropValue> list = new ArrayList<>(prodPropValues.size());
        if (row > 0) {
            prodPropValues.forEach(prodPropValue -> {
                prodPropValue.setPropId(prodProp.getPropId());
                list.add(prodPropValue);
            });
            prodPropValueService.saveBatch(list);
        }
    }

    @Override
    public List<ProdProp> getProdPropList() {
        List<ProdProp> prodProps = prodPropMapper.selectList(new LambdaQueryWrapper<>());
        // 查询属性id对应的属性值进行封装
        prodProps.forEach(p -> {
            List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>()
                    .eq(ProdPropValue::getPropId, p.getPropId()));
            p.setProdPropValues(list);
        });
        return prodProps;
    }
}
