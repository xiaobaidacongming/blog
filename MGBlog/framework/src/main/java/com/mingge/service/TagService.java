package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.ListTagDto;
import com.mingge.domain.dto.TagDto;
import com.mingge.domain.entity.Tag;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-05-12 10:01:55
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, ListTagDto tagDto);

    ResponseResult add(TagDto tagDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult listAllTag();
}

