package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/8 19:08
 */
public interface SearchService {
    Page<ProdEs> loadSearchProds(Page<ProdEs> page, ProdEs prodEs, Integer sort);
}
