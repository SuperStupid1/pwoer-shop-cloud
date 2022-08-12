package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.powernode.domain.Sku;
import com.powernode.dto.StockChangeDto;
import com.powernode.service.ProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

    /*===============================小程序接口=========================================*/

    @GetMapping("getProdById")
    @ApiOperation("根据商品id查询商品")
    public Prod getProdById(@RequestParam Long prodId){
        return prodService.getById(prodId);
    }

    @GetMapping("/prod/prodInfo")
    @ApiOperation("根据商品id查询商品")
    public ResponseEntity<Prod> getProdInfo(Long prodId){
        Prod prod = prodService.getById(prodId);
        return ResponseEntity.ok(prod);
    }


    @PostMapping("getSkuByIds")
    @ApiOperation("根据id查询sku")
    public List<Sku> getSkuByIds(@RequestBody List<Long> skuIds){
        return prodService.getSkuByIds(skuIds);
    }

    @PostMapping("stockChange")
    @ApiOperation("修改库存")
    public void changeStack(@RequestBody StockChangeDto stockChangeDto){
        prodService.changeStack(stockChangeDto);
    }


}
