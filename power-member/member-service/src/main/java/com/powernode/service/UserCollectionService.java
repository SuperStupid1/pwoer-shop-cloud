package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdEs;
import com.powernode.domain.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/4 17:47
 */
public interface UserCollectionService extends IService<UserCollection>{


        void addOrCancel(String userId, Long prodId);

        Page<ProdEs> getUserCollectionPage(String userId, Page<UserCollection> page);
    }
