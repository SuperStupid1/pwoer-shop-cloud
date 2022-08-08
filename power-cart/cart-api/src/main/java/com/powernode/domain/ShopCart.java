package com.powernode.domain;

import com.powernode.domain.CartItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 杜波
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("购物车中店铺对象")
public class ShopCart {

    @ApiModelProperty("店铺中商品条目的集合")
    private List<CartItem> shopCartItems;

    /**
     * 优惠券  折扣
     */
    @ApiModelProperty("店铺的满减优惠")
    private BigDecimal shopReduce;

    @ApiModelProperty("店铺中对应的运费")
    private BigDecimal yunfei;


}