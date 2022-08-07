package com.powernode.hystrix;

import com.powernode.domain.ProdEs;
import com.powernode.feign.CollectEsFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author 杜波
 */
@Component
@Slf4j
public class UserCollectSearchFeignHystrix implements CollectEsFeign {
    /**
     * 远程的搜索服务 拿到商品对象
     *
     * @param prodIds
     * @return
     */
    @Override
    public List<ProdEs> getProdsByIds(List<Long> prodIds) {
        log.error("远程调用失败了");
        return Collections.emptyList();
    }
}