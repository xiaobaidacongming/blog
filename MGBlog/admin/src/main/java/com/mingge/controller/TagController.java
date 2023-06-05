package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.ListTagDto;
import com.mingge.domain.dto.TagDto;
import com.mingge.domain.entity.Tag;
import com.mingge.domain.vo.TagVo;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.service.TagService;
import com.mingge.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ListTagDto tagDto){
        return tagService.pageTagList(pageNum,pageSize,tagDto);
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
    @PostMapping
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.add(tagDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);
    }
    @PutMapping
    public ResponseResult putTag(@RequestBody TagVo tagVo){
        return tagService.updateById(BeanCopyUtils.copyBean(tagVo,Tag.class))?
                ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }
}
