package com.powernode.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.OrderSettlementMapper;
import com.powernode.domain.OrderSettlement;
import com.powernode.service.OrderSettlementService;
/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/9 9:44
 */
@Service
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements OrderSettlementService{

}
