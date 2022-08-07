package com.powernode.controller;

import com.powernode.domain.User;
import com.powernode.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/4 17:56
 */
@RestController
@RequestMapping("p/user")
@Api(tags = "会员管理接口")
public class MemberController {
    @Autowired
    private UserService userService;

    @PutMapping("setUserInfo")
    @ApiOperation("更新用户的头像和昵称等")
    public ResponseEntity<Void> updateUserInfo(@RequestBody User user) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        user.setUserId(userId);
        userService.updateById(user);
        return ResponseEntity.ok().build();
    }
}
