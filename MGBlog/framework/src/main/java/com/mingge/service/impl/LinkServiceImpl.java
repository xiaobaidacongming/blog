package com.mingge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.LinkAddDto;
import com.mingge.domain.dto.LinkListDto;
import com.mingge.domain.dto.LinkModifyDto;
import com.mingge.domain.entity.Link;
import com.mingge.domain.vo.LinkListVo;
import com.mingge.domain.vo.LinkVo;
import com.mingge.domain.vo.PageVo;
import com.mingge.mapper.LinkMapper;
import com.mingge.service.LinkService;
import com.mingge.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-10-02 14:45:31
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Autowired
    private LinkMapper linkMapper;
    /**
     * 查询所有审核通过的友链
     * @return 友链集合
     */
    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        // 获取审核已通过的友链  即返回了数据库中所有的友链
        queryWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_APPROVED);
        List<Link> lists = list(queryWrapper);
        // 转换成Vo
        List<LinkVo> linkVos = BeanCopyUtils.copyListBean(lists, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult listLinks(LinkListDto linkListDto) {
        PageHelper.startPage(linkListDto.getPageNum(),linkListDto.getPageSize());
        List<LinkListVo> linksVo=linkMapper.selectLinks(linkListDto);
        PageInfo<LinkListVo> pageInfo = new PageInfo<>(linksVo);
        return ResponseResult.okResult(new PageVo(pageInfo.getList(),pageInfo.getTotal()));
    }

    @Override
    public ResponseResult addLink(LinkAddDto linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto,Link.class);
        linkMapper.insert(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult modifyLink(LinkModifyDto linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        linkMapper.updateById(link);
        return ResponseResult.okResult();
    }
}

