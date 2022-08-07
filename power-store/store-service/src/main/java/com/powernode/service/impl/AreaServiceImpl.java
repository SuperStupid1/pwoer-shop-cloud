package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.constant.AreaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.AreaMapper;
import com.powernode.domain.Area;
import com.powernode.service.AreaService;
/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.AreaServiceImpl")
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService{

    @Autowired
    private AreaMapper areaMapper;


    @Override
    @Cacheable(key = AreaConstant.AREA_PREFIX)
    public List<Area> list(Wrapper<Area> queryWrapper) {
        return areaMapper.selectList(queryWrapper);
    }

    /**
     * 根据父id查询子区域集合
     *
     * @param pid
     * @return
     */
    @Override
    public List<Area> listByPid(Long pid) {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>()
                .eq(Area::getParentId, pid)
        );
    }
}
