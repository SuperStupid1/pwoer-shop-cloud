package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.annotation.Log;
import com.powernode.domain.SysUser;
import com.powernode.mapper.SysUserMapper;
import com.powernode.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/26 17:22
 */
@RestController
@RequestMapping("sys/user")
@Api(tags = "后台管理员接口")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("info")
    @ApiOperation("获取当前登录用户信息")
    public ResponseEntity<SysUser> getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().toString();
        SysUser sysUser = sysUserService.getById(userId);
        return ResponseEntity.ok(sysUser);
    }

    @GetMapping("page")
    @ApiOperation("分页查询管理员")
    @PreAuthorize("hasAuthority('sys:user:page')")
    @Log(operation = "分页查询管理员")
    public ResponseEntity<Page<SysUser>> getAdmin(Page<SysUser> page) {
        Page<SysUser> sysUserPage = sysUserService.page(page);
        return ResponseEntity.ok(sysUserPage);
    }

    @PostMapping
    @ApiOperation("添加管理员")
    @Log(operation = "添加管理员")
    public ResponseEntity addAdmin(SysUser sysUser){
        sysUserService.addSysUser(sysUser);
        return (ResponseEntity) ResponseEntity.ok();
    }


}
