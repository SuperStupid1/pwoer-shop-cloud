package com.powernode.feign;

import com.powernode.domain.Sku;
import com.powernode.dto.StockChangeDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/9 14:41
 */
@FeignClient(value = "product-service")
public interface OrderProdFeign {

    /**
     * 根据id查询sku
     * @param skuIds
     * @return
     */
    @PostMapping("prod/prod/getSkuByIds")
    @ApiOperation("根据id查询sku")
    List<Sku> getSkuByIds(@RequestBody List<Long> skuIds);

    @PostMapping("prod/prod/stockChange")
    void changeStack(@RequestBody StockChangeDto stockChangeDto);
}
