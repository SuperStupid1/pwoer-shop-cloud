package com.powernode.service;

import com.powernode.domain.Basket;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/6 15:40
 */
public interface BasketService extends IService<Basket>{


        Integer findUserBasketCount(String userId);
    }
