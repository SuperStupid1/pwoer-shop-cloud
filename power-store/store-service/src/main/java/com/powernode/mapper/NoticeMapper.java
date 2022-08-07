package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 信息描述
 * @author DuBo
 * @createDate 2022/8/2 18:26
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}