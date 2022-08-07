package com.powernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
@ApiModel(value="com-powernode-domain-TransfeeFree")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "transfee_free")
public class TransfeeFree implements Serializable {
    /**
     * 指定条件包邮项id
     */
    @TableId(value = "transfee_free_id", type = IdType.AUTO)
    @ApiModelProperty(value="指定条件包邮项id")
    private Long transfeeFreeId;

    /**
     * 运费模板id
     */
    @TableField(value = "transport_id")
    @ApiModelProperty(value="运费模板id")
    private Long transportId;

    /**
     * 包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
     */
    @TableField(value = "free_type")
    @ApiModelProperty(value="包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）")
    private Integer freeType;

    /**
     * 需满金额
     */
    @TableField(value = "amount")
    @ApiModelProperty(value="需满金额")
    private BigDecimal amount;

    /**
     * 包邮x件/重量/体积
     */
    @TableField(value = "piece")
    @ApiModelProperty(value="包邮x件/重量/体积")
    private BigDecimal piece;

    private static final long serialVersionUID = 1L;

    public static final String COL_TRANSFEE_FREE_ID = "transfee_free_id";

    public static final String COL_TRANSPORT_ID = "transport_id";

    public static final String COL_FREE_TYPE = "free_type";

    public static final String COL_AMOUNT = "amount";

    public static final String COL_PIECE = "piece";
}