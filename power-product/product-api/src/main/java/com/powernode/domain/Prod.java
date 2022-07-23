package com.powernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.powernode.dto.DeliveryModeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/7/28 15:20
 */
/**
    * 商品
    */
@ApiModel(value="com-powernode-domain-Prod")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "prod")
public class Prod implements Serializable {
    /**
     * 产品ID
     */
    @TableId(value = "prod_id", type = IdType.AUTO)
    @ApiModelProperty(value="产品ID")
    private Long prodId;

    /**
     * 商品名称
     */
    @TableField(value = "prod_name")
    @ApiModelProperty(value="商品名称")
    private String prodName;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="店铺id")
    private Long shopId;

    /**
     * 原价
     */
    @TableField(value = "ori_price")
    @ApiModelProperty(value="原价")
    private BigDecimal oriPrice;

    /**
     * 现价
     */
    @TableField(value = "price")
    @ApiModelProperty(value="现价")
    private BigDecimal price;

    /**
     * 简要描述,卖点等
     */
    @TableField(value = "brief")
    @ApiModelProperty(value="简要描述,卖点等")
    private String brief;

    /**
     * 详细描述
     */
    @TableField(value = "content")
    @ApiModelProperty(value="详细描述")
    private String content;

    /**
     * 商品主图
     */
    @TableField(value = "pic")
    @ApiModelProperty(value="商品主图")
    private String pic;

    /**
     * 商品图片，以,分割
     */
    @TableField(value = "imgs")
    @ApiModelProperty(value="商品图片，以,分割")
    private String imgs;

    /**
     * 默认是1，表示正常状态, -1表示删除, 0下架
     */
    @TableField(value = "status")
    @ApiModelProperty(value="默认是1，表示正常状态, -1表示删除, 0下架")
    private Integer status;

    /**
     * 商品分类
     */
    @TableField(value = "category_id")
    @ApiModelProperty(value="商品分类")
    private Long categoryId;

    /**
     * 销量
     */
    @TableField(value = "sold_num")
    @ApiModelProperty(value="销量")
    private Integer soldNum;

    /**
     * 总库存
     */
    @TableField(value = "total_stocks")
    @ApiModelProperty(value="总库存")
    private Integer totalStocks;

    /**
     * 配送方式json见TransportModeVO
     */
    @TableField(value = "delivery_mode")
    @ApiModelProperty(value="配送方式json见TransportModeVO")
    private String deliveryMode;

    /**
     * 运费模板id
     */
    @TableField(value = "delivery_template_id")
    @ApiModelProperty(value="运费模板id")
    private Long deliveryTemplateId;

    /**
     * 录入时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="录入时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;

    /**
     * 上架时间
     */
    @TableField(value = "putaway_time")
    @ApiModelProperty(value="上架时间")
    private LocalDateTime putawayTime;

    /**
     * 版本 乐观锁
     */
    @TableField(value = "version")
    @ApiModelProperty(value="版本 乐观锁")
    private Integer version;


    @TableField(exist = false)
    @ApiModelProperty(value = "标签分组ids集合")
    private List<Long> tagList;

    @TableField(exist = false)
    @ApiModelProperty(value = "sku集合")
    private List<Sku> skuList;


    @TableField(exist = false)
    @ApiModelProperty(value = "配送方式")
    private DeliveryModeVo deliveryModeVo;

    private static final long serialVersionUID = 1L;

    public static final String COL_PROD_ID = "prod_id";

    public static final String COL_PROD_NAME = "prod_name";

    public static final String COL_SHOP_ID = "shop_id";

    public static final String COL_ORI_PRICE = "ori_price";

    public static final String COL_PRICE = "price";

    public static final String COL_BRIEF = "brief";

    public static final String COL_CONTENT = "content";

    public static final String COL_PIC = "pic";

    public static final String COL_IMGS = "imgs";

    public static final String COL_STATUS = "status";

    public static final String COL_CATEGORY_ID = "category_id";

    public static final String COL_SOLD_NUM = "sold_num";

    public static final String COL_TOTAL_STOCKS = "total_stocks";

    public static final String COL_DELIVERY_MODE = "delivery_mode";

    public static final String COL_DELIVERY_TEMPLATE_ID = "delivery_template_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_PUTAWAY_TIME = "putaway_time";

    public static final String COL_VERSION = "version";
}