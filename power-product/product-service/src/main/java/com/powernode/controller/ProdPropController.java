package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdProp;
import com.powernode.domain.ProdPropValue;
import com.powernode.service.ProdPropService;
import com.powernode.service.ProdPropValueService;
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
 * @createDate 2022/7/28 17:42
 */
@RestController
@RequestMapping("prod/spec")
@Api(tags = "商品规格属性接口")
public class ProdPropController {

    @Autowired
    private ProdPropService prodPropService;

    @Autowired
    private ProdPropValueService prodPropValueService;

    @GetMapping("page")
    @ApiOperation("分页查询商品规格属性")
    public ResponseEntity<Page<ProdProp>> findProdPropPage(Page<ProdProp> page,ProdProp prodProp){
        Page<ProdProp> prodPropPage = prodPropService.findProdPropPage(page,prodProp);
        return ResponseEntity.ok(prodPropPage);
    }

    @PostMapping
    @ApiOperation("添加商品属性")
    public ResponseEntity<Void> addProdProp(@RequestBody ProdProp prodProp){
        prodPropService.saveProdProp(prodProp);
        return ResponseEntity.ok().build();
    }

    @GetMapping("list")
    @ApiOperation("查询全部商品规格属性")
    public ResponseEntity<List<ProdProp>> getProdPropList(){
        List<ProdProp> list = prodPropService.getProdPropList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("listSpecValue/{id}")
    @ApiOperation("根据属性id查询属性值")
    public ResponseEntity<List<ProdPropValue>> getProdPropValueList(@PathVariable("id") Long id){
        List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>()
                .eq(!ObjectUtils.isEmpty(id),ProdPropValue::getPropId,id)
        );
        return ResponseEntity.ok(list);
    }


}
