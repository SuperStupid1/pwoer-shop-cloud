package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.dto.ProdCommOverview;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
public interface ProdCommService extends IService<ProdComm>{


        Page<ProdComm> findProdCommPage(Page<ProdComm> page, ProdComm prodComm);

        ProdCommOverview loadProdCommByProdId(Long prodId);

    Page<ProdComm> loadProdCommByEvaluate(Page<ProdComm> page, Long prodId, Integer evaluate);
}
