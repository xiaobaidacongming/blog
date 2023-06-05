package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-22 11:31:14
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    List<Long> selectTagIdByArticleId(@Param("id") Long id);
}

