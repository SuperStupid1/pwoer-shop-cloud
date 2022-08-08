package com.powernode.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 杜波
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("购物车中商品条目对象")
public class CartItem {

    @ApiModelProperty("购物车的id")
    private Long basketId;

    @ApiModelProperty("该条目是否被选择")
    private Boolean checked;

    @ApiModelProperty("该条目对应的商品id")
    private Long prodId;

    @ApiModelProperty("该条目对应的商品的名称")
    private String prodName;

    @ApiModelProperty("该条目SkuId")
    private Long skuId;

    @ApiModelProperty("该条目的名称")
    private String skuName;

    @ApiModelProperty("该条目对应的主图")
    private String pic;

    @ApiModelProperty("该条目对应的价格")
    private BigDecimal price;

    @ApiModelProperty("该条目对应数量")
    private Integer basketCount;
}