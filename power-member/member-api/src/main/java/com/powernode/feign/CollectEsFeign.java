package com.powernode.feign;

import com.powernode.domain.ProdEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "search-service")
public interface CollectEsFeign {

    @PostMapping("/getProdsByIds")
    List<ProdEs> getProdsByIds(@RequestBody List<Long> prodIds);

}