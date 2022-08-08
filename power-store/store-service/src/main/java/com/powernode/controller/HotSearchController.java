package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.HotSearch;
import com.powernode.service.HotSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/3 15:58
 */
@RestController
@RequestMapping("admin/hotSearch")
@Api(tags = "热搜接口")
public class HotSearchController {

    @Autowired
    private HotSearchService hotSearchService;

    @GetMapping("page")
    @ApiOperation("分页查询热搜")
    public ResponseEntity<Page<HotSearch>> loadHotSearch(Page<HotSearch> page, HotSearch hotSearch) {
        Page<HotSearch> searchPage = hotSearchService.page(page, new LambdaQueryWrapper<HotSearch>()
                .eq(!ObjectUtils.isEmpty(hotSearch.getStatus()), HotSearch::getStatus, hotSearch.getStatus())
                .like(StringUtils.hasText(hotSearch.getTitle()), HotSearch::getTitle, hotSearch.getTitle())
                .like(StringUtils.hasText(hotSearch.getContent()), HotSearch::getContent, hotSearch.getContent())
                .orderByDesc(HotSearch::getSeq)
        );
        return ResponseEntity.ok(searchPage);
    }

    @PostMapping
    @ApiOperation("新增热搜")
    public ResponseEntity<Void> loadHotSearchPage(@RequestBody HotSearch hotSearch) {
        hotSearchService.save(hotSearch);
        return ResponseEntity.ok().build();
    }

    @GetMapping("hotSearchByShopId")
    @ApiOperation("加载热搜")
    public ResponseEntity<List<HotSearch>> loadHotSearch(@RequestParam Long shopId){
        List<HotSearch> list = hotSearchService.list(new LambdaQueryWrapper<HotSearch>().eq(HotSearch::getShopId, shopId));
        return ResponseEntity.ok(list);
    }
}
