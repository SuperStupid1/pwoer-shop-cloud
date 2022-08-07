package com.powernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/3 21:25
 */
/**
    * 用户表
    */
@ApiModel(value="com-powernode-domain-WxUser")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user`")
public class WxUser implements Serializable, UserDetails {
    /**
     * ID
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    @ApiModelProperty(value="ID")
    private String userId;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value="用户昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty(value="真实姓名")
    private String realName;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_mail")
    @ApiModelProperty(value="用户邮箱")
    private String userMail;

    /**
     * 登录密码
     */
    @TableField(value = "login_password")
    @ApiModelProperty(value="登录密码")
    private String loginPassword;

    /**
     * 支付密码
     */
    @TableField(value = "pay_password")
    @ApiModelProperty(value="支付密码")
    private String payPassword;

    /**
     * 手机号码
     */
    @TableField(value = "user_mobile")
    @ApiModelProperty(value="手机号码")
    private String userMobile;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time")
    @ApiModelProperty(value="修改时间")
    private LocalDateTime modifyTime;

    /**
     * 注册时间
     */
    @TableField(value = "user_regtime")
    @ApiModelProperty(value="注册时间")
    private LocalDateTime userRegtime;

    /**
     * 注册IP
     */
    @TableField(value = "user_regip")
    @ApiModelProperty(value="注册IP")
    private String userRegip;

    /**
     * 最后登录时间
     */
    @TableField(value = "user_lasttime")
    @ApiModelProperty(value="最后登录时间")
    private LocalDateTime userLasttime;

    /**
     * 最后登录IP
     */
    @TableField(value = "user_lastip")
    @ApiModelProperty(value="最后登录IP")
    private String userLastip;

    /**
     * 备注
     */
    @TableField(value = "user_memo")
    @ApiModelProperty(value="备注")
    private String userMemo;

    /**
     * M(男) or F(女)
     */
    @TableField(value = "sex")
    @ApiModelProperty(value="M(男) or F(女)")
    private String sex;

    /**
     * 例如：2009-11-27
     */
    @TableField(value = "birth_date")
    @ApiModelProperty(value="例如：2009-11-27")
    private String birthDate;

    /**
     * 头像图片路径
     */
    @TableField(value = "pic")
    @ApiModelProperty(value="头像图片路径")
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态 1 正常 0 无效")
    private Integer status;

    /**
     * 用户积分
     */
    @TableField(value = "score")
    @ApiModelProperty(value="用户积分")
    private Integer score;

    private static final long serialVersionUID = 1L;

    public static final String COL_USER_ID = "user_id";

    public static final String COL_NICK_NAME = "nick_name";

    public static final String COL_REAL_NAME = "real_name";

    public static final String COL_USER_MAIL = "user_mail";

    public static final String COL_LOGIN_PASSWORD = "login_password";

    public static final String COL_PAY_PASSWORD = "pay_password";

    public static final String COL_USER_MOBILE = "user_mobile";

    public static final String COL_MODIFY_TIME = "modify_time";

    public static final String COL_USER_REGTIME = "user_regtime";

    public static final String COL_USER_REGIP = "user_regip";

    public static final String COL_USER_LASTTIME = "user_lasttime";

    public static final String COL_USER_LASTIP = "user_lastip";

    public static final String COL_USER_MEMO = "user_memo";

    public static final String COL_SEX = "sex";

    public static final String COL_BIRTH_DATE = "birth_date";

    public static final String COL_PIC = "pic";

    public static final String COL_STATUS = "status";

    public static final String COL_SCORE = "score";

    /**
     * 前台用户没有权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "$2a$10$F/el/1kNa9uJ8q27kcIpvelMmYv0qYF4Yx9aud8HoZG73VczLxH9.";
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }
}