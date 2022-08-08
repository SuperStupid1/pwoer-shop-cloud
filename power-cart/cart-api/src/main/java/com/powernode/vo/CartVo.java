package com.powernode.vo;

import com.powernode.domain.ShopCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/8 15:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartVo {

    private List<ShopCart> shopCarts = new ArrayList<>();

}
