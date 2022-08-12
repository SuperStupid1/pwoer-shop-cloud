package com.powernode.domain;

import com.powernode.dto.StockChangeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送到延迟队列的超时订单封装
 *
 * @author DuBo
 * @createDate 2022/8/12 14:50
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OvertimeOrderDto {

    private StockChangeDto stockChangeDto;
    private String orderSn;
}
