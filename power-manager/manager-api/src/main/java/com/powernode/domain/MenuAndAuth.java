package com.powernode.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("菜单和权限的对象")
public class MenuAndAuth {

    @ApiModelProperty("菜单集合")
    private List<SysMenu> menuList;

    @ApiModelProperty("权限集合")
    private List<String> authorities;


}