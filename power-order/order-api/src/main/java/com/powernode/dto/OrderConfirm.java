package com.powernode.dto;

import com.powernode.domain.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 杜波
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单确认的入参对象")
public class OrderConfirm {

    @ApiModelProperty("购物车的ids")
    private List<Long> basketIds;

    @ApiModelProperty("订单条目对象")
    private OrderItem orderItem;

}