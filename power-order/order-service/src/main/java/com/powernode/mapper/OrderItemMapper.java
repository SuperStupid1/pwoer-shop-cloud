package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/9 9:44
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}