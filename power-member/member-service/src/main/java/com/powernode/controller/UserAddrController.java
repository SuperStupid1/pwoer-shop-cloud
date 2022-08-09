package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.powernode.domain.UserAddr;
import com.powernode.service.UserAddrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/4 18:13
 */
@RestController
@RequestMapping("p/address")
@Api(tags = "用户的收货地址")
public class UserAddrController {

    @Autowired
    private UserAddrService userAddrService;

    @GetMapping("list")
    @ApiOperation("查询用户的所有收货地址")
    public ResponseEntity<List<UserAddr>> getUserAddr() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<UserAddr> userAddrs = userAddrService.findUserAddr(userId);
        return ResponseEntity.ok(userAddrs);
    }

    @PostMapping
    @ApiOperation("新增用户的收货地址")
    public ResponseEntity<Void> addUserAddr(@RequestBody UserAddr userAddr) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userAddr.setUserId(userId);
        userAddrService.save(userAddr);
        return ResponseEntity.ok().build();
    }

    @PutMapping("defaultAddr/{addrId}")
    @ApiOperation("修改默认地址")
    public ResponseEntity<Void> updateDefaultAddr(@PathVariable Long addrId) {
        userAddrService.updateDefaultAddr(addrId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("addrInfo/{addrId}")
    @ApiOperation("获取地址信息")
    public ResponseEntity<UserAddr> findAddrById(@PathVariable Long addrId) {
        UserAddr userAddr = userAddrService.getById(addrId);
        return ResponseEntity.ok(userAddr);
    }

    @PutMapping("updateAddr")
    @ApiOperation("更新收货地址")
    public ResponseEntity<Void> updateUserAddr(@RequestBody UserAddr userAddr) {
        userAddrService.updateById(userAddr);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("根据用户查询默认地址")
    @PostMapping("getDefaultAddr")
    public UserAddr getDefaultAddrByUserId(@RequestBody String userId) {
        return userAddrService.getOne(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userId)
                .eq(UserAddr::getStatus, 1)
                .eq(UserAddr::getCommonAddr, 1)
        );
    }
}
