package com.powernode.service;

import com.powernode.domain.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
public interface AreaService extends IService<Area>{


        List<Area> listByPid(Long pid);
    }
