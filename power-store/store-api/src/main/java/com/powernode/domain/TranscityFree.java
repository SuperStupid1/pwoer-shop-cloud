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
@ApiModel(value="com-powernode-domain-TranscityFree")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "transcity_free")
public class TranscityFree implements Serializable {
    /**
     * 指定条件包邮城市项id
     */
    @TableId(value = "transcity_free_id", type = IdType.AUTO)
    @ApiModelProperty(value="指定条件包邮城市项id")
    private Long transcityFreeId;

    /**
     * 指定条件包邮项id
     */
    @TableField(value = "transfee_free_id")
    @ApiModelProperty(value="指定条件包邮项id")
    private Long transfeeFreeId;

    /**
     * 城市id
     */
    @TableField(value = "free_city_id")
    @ApiModelProperty(value="城市id")
    private Long freeCityId;

    private static final long serialVersionUID = 1L;

    public static final String COL_TRANSCITY_FREE_ID = "transcity_free_id";

    public static final String COL_TRANSFEE_FREE_ID = "transfee_free_id";

    public static final String COL_FREE_CITY_ID = "free_city_id";
}