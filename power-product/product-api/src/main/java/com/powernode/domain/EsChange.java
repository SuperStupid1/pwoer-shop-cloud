package com.powernode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装商品库存变化的数据
 *
 * @author DuBo
 * @createDate 2022/8/2 18:01
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsChange {

    private Long prodId;

    /**
     * 商品变化的数量 符号代表增减
     */
    private Long count;
}
