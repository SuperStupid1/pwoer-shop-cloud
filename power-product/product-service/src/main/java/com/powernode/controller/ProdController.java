package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.powernode.service.ProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:24
 */
@RestController
@Api(tags = "产品信息接口")
@RequestMapping("prod/prod")
public class ProdController {

    @Autowired
    private ProdService prodService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prod:page')")
    @ApiOperation("分页查询产品")
    public ResponseEntity<Page<Prod>> getProdPage(Page<Prod> prodPage,Prod prod){
        Page<Prod> page = prodService.findProdPage(prodPage,prod);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('prod:prod:save')")
    @ApiOperation("添加产品")
    public ResponseEntity<Void> addProd(@RequestBody Prod prod){
        prodService.saveProd(prod);
        return ResponseEntity.ok().build();
    }


}
