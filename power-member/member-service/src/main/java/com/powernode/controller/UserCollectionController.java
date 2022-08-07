package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;
import com.powernode.domain.UserCollection;
import com.powernode.service.UserCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户的收藏
 *
 * @author DuBo
 * @createDate 2022/8/5 19:52
 */
@RestController
@RequestMapping("p/collection")
@Api(tags = "用户的收藏")
public class UserCollectionController {

    @Autowired
    private UserCollectionService userCollectionService;

    @GetMapping("count")
    @ApiOperation("查询当前用户的收藏商品数量")
    public ResponseEntity<Integer> getUserCollection() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        int count = userCollectionService.count(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
        );
        return ResponseEntity.ok(count);
    }

    @GetMapping("isCollection")
    @ApiOperation("查询当前用户是否收藏该商品")
    public ResponseEntity<Boolean> isCollection(Long prodId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        int count = userCollectionService.count(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getProdId, prodId)
        );
        return ResponseEntity.ok(count > 0);
    }

    @PostMapping("addOrCancel")
    @ApiOperation("添加或者取消收藏")
    public ResponseEntity<Void> addOrCancel(@RequestBody Long prodId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userCollectionService.addOrCancel(userId, prodId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("prods")
    @ApiOperation("分页查询用户的收藏")
    public ResponseEntity<Page<ProdEs>> getUserCollectionPage(Page<UserCollection> page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Page<ProdEs> prodEsPage = userCollectionService.getUserCollectionPage(userId, page);
        return ResponseEntity.ok(prodEsPage);
    }
}
