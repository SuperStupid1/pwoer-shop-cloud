package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/5 16:16
 */
public interface ProdSearchService {
    Page<ProdEs> searchProdEsByTagId(Long tagId, Integer size, Integer current);
}
