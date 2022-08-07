package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.User;
import com.powernode.mapper.UserMapper;
import com.powernode.service.UserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/4 17:47
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private UserMapper userMapper;



    /**
     * 更新用户的头像和昵称等
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateById(User user) {
        log.info("更新用户的信息{}", JSON.toJSONString(user));
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = requestAttributes.getRequest().getRemoteAddr();
        user.setUserLastip(ip);
        user.setModifyTime(LocalDateTime.now());
        user.setSex(user.getSex().equals("1") ? "M" : "F");
        user.setUserLasttime(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }

}
