package com.powernode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存修改的传输对象
 *
 * @author DuBo
 * @createDate 2022/8/11 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DbStockChange {

    private Long id;

    private Integer count;
}
