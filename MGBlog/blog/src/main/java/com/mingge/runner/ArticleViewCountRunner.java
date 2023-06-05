package com.mingge.runner;

import com.mingge.constants.SystemConstants;
import com.mingge.domain.entity.Article;
import com.mingge.mapper.ArticleMapper;
import com.mingge.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArticleViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息  id  viewCount
        Map<String,Integer> viewCountMap = new HashMap<>();
        List<Article> articleList = articleMapper.selectList(null);
        for (Article article:articleList){
            viewCountMap.put(article.getId().toString(),article.getViewCount().intValue());
        }
        // 把文章的浏览量存入到redis中
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW_COUNT,viewCountMap);
    }
}
