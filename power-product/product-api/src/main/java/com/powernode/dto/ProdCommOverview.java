package com.powernode.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品的评论总览")
public class ProdCommOverview {

    /**
     * 好评率
     */
    private BigDecimal positiveRating;
    /**
     * 总评论数
     */
    private Long number;
    /**
     * 好评数
     */
    private Long praiseNumber;
    /**
     * 中评数
     */
    private Long secondaryNumber;
    /**
     * 差评数
     */
    private Long negativeNumber;
    /**
     * 有图评论数
     */
    private Long picNumber;

}