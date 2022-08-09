package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/9 9:44
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}