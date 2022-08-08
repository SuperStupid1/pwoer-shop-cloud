package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Category;
import com.powernode.domain.Prod;
import com.powernode.service.CategoryService;
import com.powernode.service.ProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:24
 */
@RestController
@Api(tags = "产品分类信息接口")
@RequestMapping("prod/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("table")
    @PreAuthorize("hasAuthority('prod:category:page')")
    @ApiOperation("查询全部产品分类")
    public ResponseEntity<List<Category>> getProdPage(){
        List<Category> list = categoryService.list();
        return ResponseEntity.ok(list);
    }

    @GetMapping("listCategory")
    @PreAuthorize("hasAuthority('prod:category:page')")
    @ApiOperation("查询全部产品分类,返回list")
    public ResponseEntity<List<Category>> getCategoryList(){
        List<Category> list = categoryService.list();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('prod:category:save')")
    @ApiOperation("新增产品分类")
    public ResponseEntity<Void> addCategory(@RequestBody Category category){
        category.setRecTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setStatus(1);
        category.setShopId(1L);
        categoryService.save(category);
        return ResponseEntity.ok().build();
    }

    @GetMapping("category/categoryInfo")
    @ApiOperation("加载分类列表")
    public ResponseEntity<List<Category>> loadCategory(Long parentId){
        List<Category> list = categoryService.list(new LambdaQueryWrapper<Category>()
                .eq(!ObjectUtils.isEmpty(parentId), Category::getParentId, parentId)
        );
        return ResponseEntity.ok(list);
    }
}
