package com.mingge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingge.domain.entity.ArticleTag;
import com.mingge.mapper.ArticleTagMapper;
import com.mingge.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-22 11:31:16
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

