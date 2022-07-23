package com.powernode.controller;

import com.powernode.domain.MenuAndAuth;
import com.powernode.domain.SysMenu;
import com.powernode.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单管理接口
 *
 * @author DuBo
 * @createDate 2022/7/25 17:38
 */
@RestController
@Api(tags = "菜单管理接口")
@RequestMapping("sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     *  1.获取当前用户信息
     *  2.查询树菜单
     *  3.获取当前用户的权限集合
     *   4.组装返回值
     * @return
     */
    @GetMapping("nav")
    @ApiOperation("查询当前用户的菜单和权限")
    public ResponseEntity<MenuAndAuth> loadMenu(){
        // 获取登录用户id然后获取对应菜单信息（需要封装成树状结构）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().toString();
        System.out.println(userId);
        List<SysMenu> menuList = sysMenuService.getMenuById(userId);
        // 获取登录用户权限
        List<String> authorities = authentication.getAuthorities().
                stream().map(GrantedAuthority::getAuthority).
                collect(Collectors.toList());
        MenuAndAuth menuAndAuth = new MenuAndAuth();
        menuAndAuth.setAuthorities(authorities);
        menuAndAuth.setMenuList(menuList);
        return ResponseEntity.ok(menuAndAuth);
    }
}
