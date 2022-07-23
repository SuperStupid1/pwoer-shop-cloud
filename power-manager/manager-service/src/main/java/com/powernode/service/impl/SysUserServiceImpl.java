package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import com.powernode.domain.SysUserRole;
import com.powernode.mapper.SysRoleMapper;
import com.powernode.mapper.SysUserRoleMapper;
import com.powernode.service.SysRoleService;
import com.powernode.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysUser;
import com.powernode.mapper.SysUserMapper;
import com.powernode.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/25 17:18
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysUserMapper sysUserMapper;



    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addSysUser(SysUser sysUser) {
        log.error("新增管理员{}", JSON.toJSONString(sysUser));
        // 加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encode);
        sysUser.setShopId(1L);
        sysUser.setCreateTime(LocalDateTime.now());
        // 插入数据库
        int row = sysUserMapper.insert(sysUser);

        if (row != 0){
            // 插入中间表
            List<Long> roleIdList = sysUser.getRoleIdList();
            List<SysUserRole> list = new ArrayList<>(roleIdList.size());
            roleIdList.forEach(roleId ->{
                list.add(SysUserRole.builder()
                        .roleId(roleId)
                .userId(sysUser.getUserId())
                .build());
            });
            sysUserRoleService.saveBatch(list);
        }
    }
}
