package com.powernode.dto;

import com.powernode.domain.DbStockChange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/11 14:54
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockChangeDto {

    private List<DbStockChange> skuChanges = new ArrayList<>();
    private List<DbStockChange> prodChanges = new ArrayList<>();
}
