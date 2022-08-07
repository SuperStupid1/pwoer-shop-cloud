package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Notice;
import com.powernode.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/8/3 16:06
 */
@RestController
@Api(tags = "公告管理")
@RequestMapping("shop/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("page")
    @ApiOperation("分页查询公告")
    public ResponseEntity<Page<Notice>> loadNoticePage(Page<Notice> page, Notice notice) {
        Page<Notice> noticePage = noticeService.page(page, new LambdaQueryWrapper<Notice>()
                .eq(!ObjectUtils.isEmpty(notice.getStatus()), Notice::getStatus, notice.getStatus())
                .eq(!ObjectUtils.isEmpty(notice.getIsTop()), Notice::getIsTop, notice.getIsTop())
                .like(StringUtils.hasText(notice.getTitle()), Notice::getTitle, notice.getTitle())
                .orderByDesc(Notice::getPublishTime)
        );
        return ResponseEntity.ok(noticePage);
    }

    /*===============================小程序接口==============================================*/

    /**
     * 小程序加载公告
     * @return
     */
    @GetMapping("topNoticeList")
    @ApiOperation("小程序加载公告")
    public List<Notice> appletLoadNotice(){
        return noticeService.list();
    }

}
