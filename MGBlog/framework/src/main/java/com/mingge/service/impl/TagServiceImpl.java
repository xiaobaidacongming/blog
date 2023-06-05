package com.mingge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.ListTagDto;
import com.mingge.domain.dto.TagDto;
import com.mingge.domain.entity.Tag;
import com.mingge.domain.vo.PageVo;
import com.mingge.domain.vo.TagListAllVo;
import com.mingge.domain.vo.TagVo;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import com.mingge.mapper.TagMapper;
import com.mingge.service.TagService;
import com.mingge.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-12 10:01:55
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, ListTagDto tagDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(StringUtils.hasText(tagDto.getName()),Tag::getName,tagDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagDto.getRemark()),Tag::getRemark,tagDto.getRemark());
        Page<Tag> page = new Page<>();
        page.setPages(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);
        // 封装数据返回
        // 查询到的数据会封装到page.getRecords()中
        List<Tag> tags = page.getRecords();
        List<TagVo> tagVos = BeanCopyUtils.copyListBean(tags, TagVo.class);
        PageVo pageVo = new PageVo(tagVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult add(TagDto tagDto) {
        if (!StringUtils.hasText(tagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NULL);
        }
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        return save(tag)? ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        if (id == null) throw new SystemException(AppHttpCodeEnum.ID_NULL);
        return removeById(id)?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }

    @Override
    public ResponseResult getTag(Long id) {
        if (id == null) throw new SystemException(AppHttpCodeEnum.ID_NULL);
        Tag tag = getById(id);
        if (tag == null) throw new SystemException(AppHttpCodeEnum.TAG_NULL);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag, TagVo.class));
    }

    @Override
    public ResponseResult listAllTag() {
        List<TagListAllVo> tagListAllVos = tagMapper.tagListAllVo();
        return ResponseResult.okResult(tagListAllVos);
    }
}

