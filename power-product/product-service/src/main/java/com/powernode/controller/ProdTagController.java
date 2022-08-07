package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.powernode.domain.ProdTag;
import com.powernode.service.ProdService;
import com.powernode.service.ProdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:24
 */
@RestController
@Api(tags = "产品分组信息接口")
@RequestMapping("prod/prodTag")
public class ProdTagController {

    @Autowired
    private ProdTagService prodTagService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prodTag:page')")
    @ApiOperation("分页查询产品分组")
    public ResponseEntity<Page<ProdTag>> getProdPage(Page<ProdTag> page,ProdTag prodTag){
        Page<ProdTag> prodTagPage = prodTagService.findProdTagPage(page,prodTag);
        return ResponseEntity.ok(prodTagPage);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('prod:prodTag:save')")
    @ApiOperation("添加产品分组")
    public ResponseEntity<Void> addProdTag(@RequestBody ProdTag prodTag){
        prodTag.setCreateTime(LocalDateTime.now());
        prodTag.setShopId(1L);
        prodTagService.save(prodTag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("listTagList")
    @ApiOperation("查询所有产品分组")
    public ResponseEntity<List<ProdTag>> getProdTagList(){
        List<ProdTag> list = prodTagService.list();
        return ResponseEntity.ok(list);
    }

    /*===============================小程序接口==============================================*/

    @GetMapping("prodTagList")
    @ApiOperation("小程序加载商品标签")
    public List<ProdTag> appletLoadProdTag(){
        return prodTagService.list();
    }


}
