package com.powernode.vo;

import com.powernode.domain.UserAddr;
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
@ApiModel("订单确认的返回对象")
public class OrderVo {

    @ApiModelProperty("用户的收货地址")
    private UserAddr userAddr;

    @ApiModelProperty("店铺集合")
    private List<ShopOrder> shopCartOrders;

    @ApiModelProperty("订单商品总数量")
    private Integer totalCount;

    @ApiModelProperty("实际金额")
    private BigDecimal actualTotal = BigDecimal.ZERO;

    @ApiModelProperty("总金额")
    private BigDecimal total = BigDecimal.ZERO;

    @ApiModelProperty("满减")
    private BigDecimal shopReduce = BigDecimal.ZERO;

    @ApiModelProperty("运费")
    private BigDecimal transfee = BigDecimal.ZERO;

    @ApiModelProperty("买家留言")
    private String remarks;

    @ApiModelProperty("选择的优惠券id")
    private List<Long> couponIds;

}