package com.powernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
@ApiModel(value="com-powernode-domain-Transcity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "transcity")
public class Transcity implements Serializable {
    @TableId(value = "transcity_id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long transcityId;

    /**
     * 运费项id
     */
    @TableField(value = "transfee_id")
    @ApiModelProperty(value="运费项id")
    private Long transfeeId;

    /**
     * 城市id
     */
    @TableField(value = "city_id")
    @ApiModelProperty(value="城市id")
    private Long cityId;

    private static final long serialVersionUID = 1L;

    public static final String COL_TRANSCITY_ID = "transcity_id";

    public static final String COL_TRANSFEE_ID = "transfee_id";

    public static final String COL_CITY_ID = "city_id";
}