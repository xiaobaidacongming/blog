package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper extends BaseMapper<Article> {
    void updateViewCountByIdAfter(@Param("articleList") List<Article> articleList);
}
