package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.SysLoginUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/22 20:41
 */
@Mapper
public interface SysLoginUserMapper extends BaseMapper<SysLoginUser> {
    /**
     * 通过登录用户名查询用户
     * @param username
     * @return
     */
    SysLoginUser getByUsername(String username);

    List<String> selectAuthById(Long userId);
}