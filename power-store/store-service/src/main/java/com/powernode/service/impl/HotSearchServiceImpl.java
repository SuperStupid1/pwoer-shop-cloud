package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.HotSearch;
import com.powernode.mapper.HotSearchMapper;
import com.powernode.service.HotSearchService;
/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
@Service
@Slf4j
public class HotSearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch> implements HotSearchService{

    @Autowired
    private HotSearchMapper hotSearchMapper;


    @Override
    public boolean save(HotSearch hotSearch) {
        log.info("新增热搜{}", JSON.toJSONString(hotSearch));
        hotSearch.setRecDate(LocalDateTime.now());
        hotSearch.setShopId(1L);
        return hotSearchMapper.insert(hotSearch) > 0;
    }
}
