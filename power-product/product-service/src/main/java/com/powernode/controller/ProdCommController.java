package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdComm;
import com.powernode.domain.ProdTag;
import com.powernode.service.ProdCommService;
import com.powernode.service.ProdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/28 15:24
 */
@RestController
@Api(tags = "产品评论信息接口")
@RequestMapping("prod/prodComm")
public class ProdCommController {

    @Autowired
    private ProdCommService prodCommService;

    @GetMapping("page")
    @ApiOperation("分页查询产品评论")
    public ResponseEntity<Page<ProdComm>> getProdPage(Page<ProdComm> page,ProdComm prodComm){
        Page<ProdComm> prodCommPage = prodCommService.findProdCommPage(page,prodComm);
        return ResponseEntity.ok(prodCommPage);
    }
}
