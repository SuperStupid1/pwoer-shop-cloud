package com.powernode.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/13 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPayDto {

    private String orderNumber;

    private Integer payType;
}
