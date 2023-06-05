package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.ArticleDto;
import com.mingge.domain.dto.ArticleListDto;
import com.mingge.domain.entity.Article;
import com.mingge.domain.vo.ArticleByIdVo;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.service.ArticleService;

import com.mingge.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }
    @GetMapping("/list")
    public ResponseResult list(ArticleListDto articleListDto){
        return articleService.getArticleList(articleListDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable("id") Long id){
        return articleService.getArticle(id);
    }
    @PutMapping
    public ResponseResult putArticle(@RequestBody ArticleByIdVo articleVo){
        return articleService.putArticle(articleVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.removeById(id)?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }

}
