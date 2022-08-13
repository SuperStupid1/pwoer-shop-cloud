package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.DbStockChange;
import com.powernode.domain.Sku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Mapper
public interface SkuMapper extends BaseMapper<Sku> {

    @Update("update sku set actual_stocks = actual_stocks + #{count},stocks = stocks - #{count}  where sku_id = #{id} and actual_stocks + #{count} >= 0;")
    int updateStack(DbStockChange dbStockChange);
}