package com.powernode.service;

import com.powernode.domain.UserAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/4 17:47
 */
public interface UserAddrService extends IService<UserAddr>{


        List<UserAddr> findUserAddr(String userId);

    void updateDefaultAddr(Long addrId);
}
