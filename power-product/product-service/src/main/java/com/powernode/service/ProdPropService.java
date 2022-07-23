package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdProp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
public interface ProdPropService extends IService<ProdProp>{


        Page<ProdProp> findProdPropPage(Page<ProdProp> page, ProdProp prodProp);

        void saveProdProp(ProdProp prodProp);

        List<ProdProp> getProdPropList();
    }
