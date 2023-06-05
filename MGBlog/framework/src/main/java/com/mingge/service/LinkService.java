package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.LinkAddDto;
import com.mingge.domain.dto.LinkListDto;
import com.mingge.domain.dto.LinkModifyDto;
import com.mingge.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-10-02 14:45:31
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLinks(LinkListDto linkListDto);

    ResponseResult addLink(LinkAddDto linkDto);

    ResponseResult modifyLink(LinkModifyDto linkDto);
}

