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
@ApiModel(value="com-powernode-domain-Notice")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "notice")
public class Notice implements Serializable {
    /**
     * 公告id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="公告id")
    private Long id;

    /**
     * 店铺id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="店铺id")
    private Long shopId;

    /**
     * 公告标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value="公告标题")
    private String title;

    /**
     * 公告内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value="公告内容")
    private String content;

    /**
     * 状态(1:公布 0:撤回)
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态(1:公布 0:撤回)")
    private Boolean status;

    /**
     * 是否置顶
     */
    @TableField(value = "is_top")
    @ApiModelProperty(value="是否置顶")
    private Integer isTop;

    /**
     * 发布时间
     */
    @TableField(value = "publish_time")
    @ApiModelProperty(value="发布时间")
    private LocalDateTime publishTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_SHOP_ID = "shop_id";

    public static final String COL_TITLE = "title";

    public static final String COL_CONTENT = "content";

    public static final String COL_STATUS = "status";

    public static final String COL_IS_TOP = "is_top";

    public static final String COL_PUBLISH_TIME = "publish_time";

    public static final String COL_UPDATE_TIME = "update_time";
}