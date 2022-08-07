package com.powernode.dao;

import com.powernode.domain.ProdEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/31 11:13
 */
@Repository
public interface ProdEsDao extends ElasticsearchRepository<ProdEs,Long> {
}
