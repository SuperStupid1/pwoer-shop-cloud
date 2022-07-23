package com.powernode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/29 18:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryModeVo {

    private Boolean hasShopDelivery;

    private Boolean hasUserPickUp;
}
