package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.domain.Sku;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
public interface ProdService extends IService<Prod>{


        Page<Prod> findProdPage(Page<Prod> prodPage, Prod prod);

        boolean saveProd(Prod prod);

        Integer getTotalCount(LocalDateTime start,LocalDateTime end);

        List<Prod> findProdPage2Es(Page<Prod> page, LocalDateTime start,LocalDateTime end);

    List<Sku> getSkuByIds(List<Long> skuIds);
}
