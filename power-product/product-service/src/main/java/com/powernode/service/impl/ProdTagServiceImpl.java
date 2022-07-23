package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.ProdTagMapper;
import com.powernode.domain.ProdTag;
import com.powernode.service.ProdTagService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Service
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService {

    @Autowired
    private ProdTagMapper prodTagMapper;

    /**
     * 分页查询商品分组（标签）
     * @param page
     * @param prodTag
     * @return
     */
    @Override
    public Page<ProdTag> findProdTagPage(Page<ProdTag> page, ProdTag prodTag) {
        return prodTagMapper.selectPage(page, new LambdaQueryWrapper<ProdTag>()
                .eq(!ObjectUtils.isEmpty(prodTag.getStatus()), ProdTag::getStatus, prodTag.getStatus())
                .like(StringUtils.hasText(prodTag.getTitle()), ProdTag::getTitle, prodTag.getTitle())
                .orderByDesc(ProdTag::getSeq));
    }
}
