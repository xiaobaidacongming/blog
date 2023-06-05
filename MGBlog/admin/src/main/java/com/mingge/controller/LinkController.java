package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.LinkAddDto;
import com.mingge.domain.dto.LinkListDto;
import com.mingge.domain.dto.LinkModifyDto;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult listLinks(LinkListDto linkListDto){
        return linkService.listLinks(linkListDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getLink(@PathVariable("id")Long id){
        return ResponseResult.okResult(linkService.getById(id));
    }
    @PostMapping
    public ResponseResult addLink(@RequestBody LinkAddDto linkDto){
        return linkService.addLink(linkDto);
    }
    @PutMapping
    public ResponseResult modifyLink(@RequestBody LinkModifyDto linkDto){
        return linkService.modifyLink(linkDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.removeById(id) ? ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }
}
