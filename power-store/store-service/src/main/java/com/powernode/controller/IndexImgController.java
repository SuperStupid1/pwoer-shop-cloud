package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.IndexImg;
import com.powernode.service.IndexImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/2 20:06
 */
@RestController
@RequestMapping("admin/indexImg")
@Api(tags = "轮播图接口")
public class IndexImgController {

    @Autowired
    private IndexImgService indexImgService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('admin:indexImg:page')")
    @ApiOperation("查询轮播图列表")
    public ResponseEntity<Page<IndexImg>> loadIndexImg(Page<IndexImg> page, IndexImg indexImg){
        Page<IndexImg> indexImgPage = indexImgService.page(page, new LambdaQueryWrapper<IndexImg>()
                .eq(!ObjectUtils.isEmpty(indexImg.getStatus()), IndexImg::getStatus, indexImg.getStatus())
        );
        return ResponseEntity.ok(indexImgPage);
    }

    @GetMapping("info/{id}")
    @ApiOperation("根据id查询轮播图")
    @PreAuthorize("hasAuthority('admin:indexImg:info')")
    public ResponseEntity<IndexImg> loadIndexImgById(@PathVariable("id") Long id) {
        IndexImg indexImg = indexImgService.getById(id);
        return ResponseEntity.ok(indexImg);
    }

    @PostMapping
    @ApiOperation("新增轮播图")
    @PreAuthorize("hasAuthority('admin:indexImg:save')")
    public ResponseEntity<Void> addIndexImg(@RequestBody IndexImg indexImg) {
        indexImgService.save(indexImg);
        return ResponseEntity.ok().build();
    }

    /*===================================小程序接口===================================*/

    @GetMapping("indexImgs")
    @ApiOperation("小程序轮播图查询")
    public List<IndexImg> appletLoadIndexImg(){
        return indexImgService.list();
    }
}
