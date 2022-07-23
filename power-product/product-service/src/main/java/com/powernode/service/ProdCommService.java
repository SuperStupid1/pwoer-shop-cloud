package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
public interface ProdCommService extends IService<ProdComm>{


        Page<ProdComm> findProdCommPage(Page<ProdComm> page, ProdComm prodComm);
    }
