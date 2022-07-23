package com.powernode.controller;

import com.powernode.annotation.Log;
import com.powernode.domain.SysRole;
import com.powernode.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/27 12:50
 */
@RestController
@Api("角色信息接口")
@RequestMapping("sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("list")
    @ApiOperation("获取所有角色信息")
    @Log(operation = "获取所有角色信息")
    public ResponseEntity<List<SysRole>> getRoleList(){
        List<SysRole> roleList = sysRoleService.list();
        return ResponseEntity.ok(roleList);
    }
}
