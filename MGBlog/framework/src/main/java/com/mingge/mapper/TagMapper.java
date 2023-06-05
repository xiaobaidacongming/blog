package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.entity.Tag;
import com.mingge.domain.vo.TagListAllVo;

import java.util.List;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-12 10:01:53
 */
public interface TagMapper extends BaseMapper<Tag> {
    List<TagListAllVo> tagListAllVo();
}

