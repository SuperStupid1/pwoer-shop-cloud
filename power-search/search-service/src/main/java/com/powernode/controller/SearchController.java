package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;
import com.powernode.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/8 18:35
 */
@RestController
@RequestMapping("search")
@Api(tags = "搜索商品")
public class SearchController {



    @Autowired
    private SearchService searchService;

    @GetMapping("searchProdPage")
    @ApiOperation("加载搜索分页结果")
    public ResponseEntity<Page<ProdEs>> loadSearchProds(Page<ProdEs> page, ProdEs prodEs, Integer sort) {
        Page<ProdEs> prodEsPage = searchService.loadSearchProds(page,prodEs,sort);

        return ResponseEntity.ok(prodEsPage);
    }

}
