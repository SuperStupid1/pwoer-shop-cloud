package com.powernode.service;

import com.powernode.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/25 17:18
 */
public interface SysMenuService extends IService<SysMenu>{


        List<SysMenu> getMenuById(String userId);
    }
