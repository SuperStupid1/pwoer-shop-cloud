package com.powernode.service;

import com.powernode.domain.Basket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.domain.CartMoney;
import com.powernode.domain.ShopCart;
import com.powernode.vo.CartVo;

import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/6 15:40
 */
public interface BasketService extends IService<Basket>{


        Integer findUserBasketCount(String userId);

        CartVo loadBasketInfo(String userId);

    CartMoney calcuCartMoney(List<Long> basketIds);

    void addBasket(Basket basket);
}
