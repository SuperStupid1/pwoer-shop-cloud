package com.powernode.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;
import com.powernode.service.ProdSearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/5 16:17
 */
@Service
public class ProdSearchServiceImpl implements ProdSearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 从es中根据标签id分页查询商品
     * @param tagId
     * @param size
     * @param current
     * @return
     */
    @Override
    public Page<ProdEs> searchProdEsByTagId(Long tagId, Integer size, Integer current) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 分页
        if (ObjectUtils.isEmpty(current)){
            current = 1;
        }
        builder.withPageable(PageRequest.of(current-1,size));
        // 匹配标签id
        builder.withQuery(QueryBuilders.matchQuery("tagList",tagId));
        NativeSearchQuery query = builder.build();
        SearchHits<ProdEs> searchHits = elasticsearchRestTemplate.search(query, ProdEs.class);
        List<ProdEs> prodEsList = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        Page<ProdEs> prodEsPage = new Page<>(current, size);
        prodEsPage.setRecords(prodEsList);
        return prodEsPage;
    }
}
