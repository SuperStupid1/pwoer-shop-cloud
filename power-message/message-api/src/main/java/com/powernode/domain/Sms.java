package com.powernode.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 杜波
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sms {

    /**
     * 目标手机号
     */
    private String phoneNum;

    /**
     * 签名
     */
    private String sign;

    /**
     * 模板id
     */
    private String templateCode;

    /**
     * 占位符
     */
    private String templateParam;
}