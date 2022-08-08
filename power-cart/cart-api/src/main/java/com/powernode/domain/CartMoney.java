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
@ApiModel("购物车中的金额对象")
public class CartMoney {


    @ApiModelProperty("最终金额")
    private BigDecimal finalMoney = BigDecimal.ZERO;

    @ApiModelProperty("总金额")
    private BigDecimal totalMoney = BigDecimal.ZERO;


    @ApiModelProperty("优惠金额")
    private BigDecimal subtractMoney = BigDecimal.ZERO;

    @ApiModelProperty("运费")
    private BigDecimal transMoney = BigDecimal.ZERO;

}