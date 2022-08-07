package com.powernode.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.mapper.NoticeMapper;
import com.powernode.domain.Notice;
import com.powernode.service.NoticeService;
/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

}
