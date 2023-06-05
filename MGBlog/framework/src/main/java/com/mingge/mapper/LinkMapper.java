package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.dto.LinkListDto;
import com.mingge.domain.entity.Link;
import com.mingge.domain.vo.LinkListVo;

import java.util.List;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-02 14:45:29
 */
public interface LinkMapper extends BaseMapper<Link> {
    List<LinkListVo> selectLinks(LinkListDto linkListDto);
}

