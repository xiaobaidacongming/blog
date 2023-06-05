package com.mingge.job;

import com.mingge.constants.SystemConstants;
import com.mingge.domain.entity.Article;
import com.mingge.mapper.ArticleMapper;
import com.mingge.service.ArticleService;
import com.mingge.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob{
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    // 每隔5分钟同步redis中的文章浏览量到数据库
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void updateViewCount(){
        // 获取浏览量的map集合
        Map<String, Object> articleMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);
        // 转换成单列集合
        List<Article> articles = articleMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()),Long.valueOf(entry.getValue().toString())))
                .collect(Collectors.toList());
        // 批量更新数据
        articleService.updateBatchById(articles);
    }
}
