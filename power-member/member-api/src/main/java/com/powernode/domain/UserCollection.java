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
 * @createDate 2022/8/4 17:47
 */
@ApiModel(value="com-powernode-domain-UserCollection")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_collection")
public class UserCollection implements Serializable {
    /**
     * 收藏表
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="收藏表")
    private Long id;

    /**
     * 商品id
     */
    @TableField(value = "prod_id")
    @ApiModelProperty(value="商品id")
    private Long prodId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户id")
    private String userId;

    /**
     * 收藏时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="收藏时间")
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_PROD_ID = "prod_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_CREATE_TIME = "create_time";
}