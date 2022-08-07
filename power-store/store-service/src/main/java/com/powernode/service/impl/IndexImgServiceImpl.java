package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import com.powernode.domain.Prod;
import com.powernode.feign.IndexImgProdFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.IndexImg;
import com.powernode.mapper.IndexImgMapper;
import com.powernode.service.IndexImgService;
import org.springframework.util.ObjectUtils;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "com.powernode.service.impl.IndexImgServiceImpl")
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService{



    @Autowired
    private IndexImgMapper indexImgMapper;

    @Autowired
    private IndexImgProdFeign indexImgProdFeign;

    /**
     * 根据id查询轮播图
     * 1.查轮播图的表 拿到商品的id
     * 2.如果有商品的id 需要远程调用 查询商品的图片和名字
     *
     * @param id
     * @return
     */
    @Override
//    @Cacheable(key = "#id")
    public IndexImg getById(Serializable id) {
        IndexImg indexImg = indexImgMapper.selectById(id);
        if (!ObjectUtils.isEmpty(indexImg)) {
            Long prodId = indexImg.getRelation();
            if (!ObjectUtils.isEmpty(prodId)) {
                // 关联了商品 我们需要去远程调用 查询商品的图片和名字
                Prod prod = indexImgProdFeign.getProdById(prodId);
                if (!ObjectUtils.isEmpty(prod)) {
                    // 如果远程调用失败了 看情况  对实际业务影响不大的情况下 可以不用管
                    String prodName = prod.getProdName();
                    String pic = prod.getPic();
                    indexImg.setProdName(prodName);
                    indexImg.setPic(pic);
                }
            }
        }
        return indexImg;
    }

    /**
     * 新增轮播图
     *
     * @param indexImg
     * @return
     */
    @Override
//    @CacheEvict(key = IndexImgConstant.INDEX_IMF_FRONT_PREFIX)
    public boolean save(IndexImg indexImg) {
        log.info("新增轮播图{}", JSON.toJSONString(indexImg));
        indexImg.setShopId(1L);
        if (indexImg.getStatus()) {
            indexImg.setUploadTime(LocalDateTime.now());
        }
        return indexImgMapper.insert(indexImg) > 0;
    }
}
