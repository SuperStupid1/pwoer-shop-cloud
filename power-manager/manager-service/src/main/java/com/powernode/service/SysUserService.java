package com.powernode.service;

import com.powernode.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/25 17:18
 */
public interface SysUserService extends IService<SysUser>{


        void addSysUser(SysUser sysUser);
    }
