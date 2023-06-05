package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.ArticleDto;
import com.mingge.domain.entity.Article;
import com.mingge.domain.dto.ArticleListDto;
import com.mingge.domain.vo.ArticleByIdVo;

import java.util.List;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleDto articleDto);

    ResponseResult getArticleList(ArticleListDto articleListDto);

    ResponseResult putArticle(ArticleByIdVo articleVo);

    ResponseResult getArticle(Long id);
}
