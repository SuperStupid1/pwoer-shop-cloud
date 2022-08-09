package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.UserAddrMapper;
import com.powernode.domain.UserAddr;
import com.powernode.service.UserAddrService;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/4 17:47
 */
@Service
@Slf4j
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr> implements UserAddrService {

    @Autowired
    private UserAddrMapper userAddrMapper;

    /**
     * 查询用户的所有收货地址
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserAddr> findUserAddr(String userId) {
        return userAddrMapper.selectList(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userId)
                .eq(UserAddr::getStatus,1)
                .orderByDesc(UserAddr::getCommonAddr,UserAddr::getCreateTime)
        );
    }

    /**
     * 修改默认地址
     * @param addrId
     */
    @Override
    public void updateDefaultAddr(Long addrId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserAddr oldDefaultAddr = userAddrMapper.selectOne(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getCommonAddr, 1)
                .eq(UserAddr::getUserId,userId)
                .eq(UserAddr::getStatus,1)
        );
        if (oldDefaultAddr.getAddrId().equals(addrId)) {
            return;
        }

        UserAddr newDefaultAddr = userAddrMapper.selectById(addrId);
        newDefaultAddr.setCommonAddr(1);
        newDefaultAddr.setUpdateTime(LocalDateTime.now());
        int row = userAddrMapper.updateById(newDefaultAddr);
        if (row > 0){
            oldDefaultAddr.setCommonAddr(0);
            oldDefaultAddr.setUpdateTime(LocalDateTime.now());
            userAddrMapper.updateById(oldDefaultAddr);
        }
    }


    /**
     * 新增用户的收货地址
     *
     * @param userAddr
     * @return
     */
    @Override
    public boolean save(UserAddr userAddr) {
        log.info("新增用户的收货地址{}", JSON.toJSONString(userAddr));
        userAddr.setUpdateTime(LocalDateTime.now());
        userAddr.setCreateTime(LocalDateTime.now());
        userAddr.setStatus(1);
        userAddr.setVersion(0);
        // 如果是新增第一个地址 默认就是 默认收货地址
        Integer count = userAddrMapper.selectCount(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userAddr.getUserId())
                .eq(UserAddr::getCommonAddr, 1)
        );
        if (count <= 0) {
            userAddr.setCommonAddr(1);
        }
        return userAddrMapper.insert(userAddr) > 0;
    }

    @Override
    public boolean updateById(UserAddr userAddr) {
        userAddr.setUpdateTime(LocalDateTime.now());
        int row = userAddrMapper.updateById(userAddr);
        return row == 1;
    }
}
