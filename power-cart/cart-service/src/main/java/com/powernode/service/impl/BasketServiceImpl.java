package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.BasketMapper;
import com.powernode.domain.Basket;
import com.powernode.service.BasketService;
import org.springframework.util.CollectionUtils;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/6 15:40
 */
@Service
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket> implements BasketService{

    @Autowired
    private BasketMapper basketMapper;

    /**
     * 查询当前用户购物车数量
     * @param userId
     * @return
     */
    @Override
    public Integer findUserBasketCount(String userId) {
        List<Object> obj = basketMapper.selectObjs(new QueryWrapper<Basket>()
                .select(" IFNULL(sum(basket_count),0) ")
                .eq("user_id", userId)
        );
        if (!CollectionUtils.isEmpty(obj)) {
            Object o = obj.get(0);
            return Integer.parseInt(o.toString());
        }
        return 0;

    }
}
