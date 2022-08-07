package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;
import com.powernode.feign.CollectEsFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.UserCollection;
import com.powernode.mapper.UserCollectionMapper;
import com.powernode.service.UserCollectionService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/4 17:47
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService{

    @Autowired
    private UserCollectionMapper userCollectionMapper;

    @Autowired
    private CollectEsFeign collectEsFeign;

    /**
     * 添加或者取消收藏
     *
     * @param userId
     * @param prodId
     */
    @Override
    public void addOrCancel(String userId, Long prodId) {
        UserCollection oldCollect = userCollectionMapper.selectOne(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getProdId, prodId)
        );
        if (!ObjectUtils.isEmpty(oldCollect)) {
            // 把这个记录删掉
            userCollectionMapper.deleteById(oldCollect.getId());
            return;
        }
        UserCollection userCollection = new UserCollection();
        userCollection.setCreateTime(LocalDateTime.now());
        userCollection.setUserId(userId);
        userCollection.setProdId(prodId);
        userCollectionMapper.insert(userCollection);
    }



    /**
     * 分页查询用户的收藏
     * 1.查询收藏表
     * 2.拿到商品的ids
     * 3.远程调用search模块  根据商品的ids 查询到商品的集合
     *
     * @param userId
     * @param page
     * @return
     */
    @Override
    public Page<ProdEs> getUserCollectionPage(String userId, Page<UserCollection> page) {
        Page<ProdEs> prodEsPage = new Page<>(page.getCurrent(), page.getSize());
        // 查询收藏表
        Page<UserCollection> userCollectionPage = userCollectionMapper.selectPage(page, new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
                .orderByDesc(UserCollection::getCreateTime)
        );
        List<UserCollection> collectionList = userCollectionPage.getRecords();
        if (CollectionUtils.isEmpty(collectionList)) {
            prodEsPage.setTotal(0L);
            prodEsPage.setRecords(Collections.emptyList());
            return prodEsPage;
        }
        // 拿到商品的ids集合
        List<Long> prodIds = collectionList.stream()
                .map(UserCollection::getProdId)
                .collect(Collectors.toList());
        // 远程调用搜索模块
        List<ProdEs> prodEsList = collectEsFeign.getProdsByIds(prodIds);
        if (CollectionUtils.isEmpty(prodEsList)) {
            throw new RuntimeException("服务器维护中");
        }
        prodEsPage.setRecords(prodEsList);
        prodEsPage.setTotal(userCollectionPage.getTotal());
        return prodEsPage;
    }
}
