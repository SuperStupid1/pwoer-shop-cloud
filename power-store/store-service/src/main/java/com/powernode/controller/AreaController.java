package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Area;
import com.powernode.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/2 19:40
 */
@RestController
@RequestMapping("admin/area")
@Api(tags = "地址管理接口")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("list")
    @PreAuthorize("hasAuthority('admin:area:list')")
    @ApiOperation("全查询地址区域")
    public ResponseEntity<List<Area>> loadArea(Area area){
        List<Area> areaList = areaService.list(new LambdaQueryWrapper<Area>()
                .like(!ObjectUtils.isEmpty(area.getAreaName()), Area::getAreaName, area.getAreaName())
        );
        return ResponseEntity.ok(areaList);
    }

    @GetMapping("listByPid")
    @ApiOperation("小程序查询地区")
    public ResponseEntity<List<Area>> loadAreaByPid(Long pid) {
        List<Area> list = areaService.listByPid(pid);
        return ResponseEntity.ok(list);
    }
}
