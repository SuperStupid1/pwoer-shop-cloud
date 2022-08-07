package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.dao.ProdEsDao;
import com.powernode.domain.Prod;
import com.powernode.domain.ProdEs;
import com.powernode.service.ProdSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/5 15:59
 */
@RestController
@RequestMapping("prod")
@Api(tags = "搜索商品的接口")
public class ProdSearchController {

    @Autowired
    private ProdSearchService prodSearchService;

    @Autowired
    private ProdEsDao prodEsDao;

    @GetMapping("prodListByTagId")
    @ApiOperation("根据标签id分页搜索商品")
    public ResponseEntity<Page<ProdEs>> prodListByTagId(Long tagId,
                                                        Integer size,
                                                        @RequestParam(required = false, defaultValue = "1") Integer current){

        Page<ProdEs> prodEsPage = prodSearchService.searchProdEsByTagId(tagId, size, current);
        return ResponseEntity.ok(prodEsPage);
    }



    @PostMapping("getProdsByIds")
    @ApiOperation("根据商品的ids查询商品")
    List<ProdEs> getProdsByIds(@RequestBody List<Long> prodIds){
        Iterable<ProdEs> prodEsIterable = prodEsDao.findAllById(prodIds);
        List<ProdEs> prodEsList = new ArrayList<>();
        prodEsIterable.forEach(prodEsList::add);
        return prodEsList;
    }

}
