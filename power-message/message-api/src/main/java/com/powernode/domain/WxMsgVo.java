package com.powernode.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信消息对象
 * @author 杜波
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMsgVo {

    @JsonProperty(value = "touser")
    private String touser;

    @JsonProperty(value = "template_id")
    private String template_id;

    @JsonProperty(value = "url")
    private String url;

    @JsonProperty(value = "topcolor")
    private String topcolor;

    @JsonProperty(value = "data")
    private Map<String,? extends Map<String, String>> data;

    public static Map<String, String> buildData(String value, String color) {
        Map<String, String> map = new HashMap<>(2);
        map.put("value", value);
        map.put("color", color);
        return map;
    }
}