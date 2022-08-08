package com.powernode.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;
import com.powernode.service.SearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/8 19:08
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 根据商品名称搜索商品
     * @param page
     * @param prodEs
     * @param sort 排序条件
     *             0 综合排序
     *             1 销量
     *             2 价格
     * @return
     */
    @Override
    public Page<ProdEs> loadSearchProds(Page<ProdEs> page, ProdEs prodEs, Integer sort) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        String sortProp = "";
        // 根据前端传的值确定排序的方式
        switch (sort){
            case 1:
                sortProp = "soldNum";
                break;
            case 2:
                sortProp = "price";
                break;
            default:
                sortProp = "praiseNumber";
                break;
        }
        // 分页并排序
        builder.withPageable(PageRequest.of(
                new Long(page.getCurrent()).intValue() - 1,
                new Long(page.getSize()).intValue(),
                Sort.Direction.DESC,
                sortProp
                )
        );
        // 根据商品名字匹配结果
        builder.withQuery(QueryBuilders.matchQuery("prodName", prodEs.getProdName()));
        NativeSearchQuery query = builder.build();
        SearchHits<ProdEs> prodEsSearchHits = elasticsearchRestTemplate.search(query, ProdEs.class);
        List<ProdEs> prodEsList = prodEsSearchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        page.setRecords(prodEsList);
        return page;
    }
}
