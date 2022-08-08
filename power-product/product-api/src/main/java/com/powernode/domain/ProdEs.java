package com.powernode.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.math.BigDecimal;
import java.util.List;

/**
 * @author 杜波
 * @createDate 2022/7/31 13:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "product_es_cxs", shards = 1, replicas = 1, refreshInterval = "1s", createIndex = true)
public class ProdEs {


    @Id
    @Field
    @ApiModelProperty("商品的id")
    private Long prodId;

    @ApiModelProperty("商品的名称")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String prodName;

    @ApiModelProperty("商品的价格")
    @Field(type = FieldType.Double)
    private BigDecimal price;

    @ApiModelProperty("商品的销量")
    @Field(type = FieldType.Long)
    private Long soldNum;

    @ApiModelProperty("商品的买点")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String brief;

    @ApiModelProperty("商品的主图")
    @Field(type = FieldType.Text)
    private String pic;

    @ApiModelProperty("商品的状态")
    @Field(type = FieldType.Integer)
    private Integer status;

    @ApiModelProperty("商品的库存")
    @Field(type = FieldType.Integer)
    private Long totalStocks;

    @ApiModelProperty("商品的分类id")
    @Field(type = FieldType.Long)
    private Long categoryId;

    @ApiModelProperty("商品的标签")
    @Field(type = FieldType.Text)
    private List<Long> tagList;

    @ApiModelProperty("商品的好评数")
    @Field(type = FieldType.Long)
    private Long praiseNumber;

    @ApiModelProperty("商品的好评率")
    @Field(type = FieldType.Double)
    private BigDecimal positiveRating;


}