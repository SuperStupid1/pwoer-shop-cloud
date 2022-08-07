package com.powernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
/**
    * 主页轮播图
    */
@ApiModel(value="com-powernode-domain-IndexImg")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "index_img")
public class IndexImg implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "img_id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long imgId;

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="店铺ID")
    private Long shopId;

    /**
     * 图片
     */
    @TableField(value = "img_url")
    @ApiModelProperty(value="图片")
    private String imgUrl;

    /**
     * 说明文字,描述
     */
    @TableField(value = "des")
    @ApiModelProperty(value="说明文字,描述")
    private String des;

    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value="标题")
    private String title;

    /**
     * 链接
     */
    @TableField(value = "link")
    @ApiModelProperty(value="链接")
    private String link;

    /**
     * 状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态")
    private Boolean status;

    /**
     * 顺序
     */
    @TableField(value = "seq")
    @ApiModelProperty(value="顺序")
    private Integer seq;

    /**
     * 上传时间
     */
    @TableField(value = "upload_time")
    @ApiModelProperty(value="上传时间")
    private LocalDateTime uploadTime;

    /**
     * 关联
     */
    @TableField(value = "relation")
    @ApiModelProperty(value="关联")
    private Long relation;

    /**
     * 类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value="类型")
    private Integer type;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品名字")
    private String prodName;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品的图片")
    private String pic;

    private static final long serialVersionUID = 1L;

    public static final String COL_IMG_ID = "img_id";

    public static final String COL_SHOP_ID = "shop_id";

    public static final String COL_IMG_URL = "img_url";

    public static final String COL_DES = "des";

    public static final String COL_TITLE = "title";

    public static final String COL_LINK = "link";

    public static final String COL_STATUS = "status";

    public static final String COL_SEQ = "seq";

    public static final String COL_UPLOAD_TIME = "upload_time";

    public static final String COL_RELATION = "relation";

    public static final String COL_TYPE = "type";
}