package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.DbStockChange;
import com.powernode.domain.Prod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
@Mapper
public interface ProdMapper extends BaseMapper<Prod> {

    @Update("update prod set total_stocks = total_stocks + #{count},sold_num = sold_num - #{count}  where prod_id = #{id} and total_stocks + #{count} >= 0")
    int updateStock(DbStockChange dbStockChange);
}