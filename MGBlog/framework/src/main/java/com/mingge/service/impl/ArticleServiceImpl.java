package com.mingge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.ArticleDto;
import com.mingge.domain.entity.Article;
import com.mingge.domain.dto.ArticleListDto;
import com.mingge.domain.entity.ArticleTag;
import com.mingge.domain.entity.Category;
import com.mingge.domain.vo.*;
import com.mingge.mapper.ArticleMapper;
import com.mingge.mapper.ArticleTagMapper;
import com.mingge.service.ArticleService;
import com.mingge.service.CategoryService;
import com.mingge.utils.BeanCopyUtils;
import com.mingge.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //不能把草稿展示出来
        //queryWrapper.eq(Article::getStatus,0);//正式开发时不能写字面量，应定义成常量
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //要按照浏览量进行降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //查询浏览量最高的前10篇文章的信息
        Page<Article> page = new Page<>(1,10);
        page(page,queryWrapper);
        List<Article> articleList = page.getRecords();

        //当前的 Article 对象封装了许多无用的信息，甚至是重要信息 这些信息无需返回到前端
        //解决方案：使用vo对象，调用bean拷贝方法，返回vo对象的集合即可
//        List<HotArticleVo> hotArticleVoList=new ArrayList<>();
//        for(Article article: articleList){
//            HotArticleVo hotArticleVo = new HotArticleVo();
//            // 这个方法要求属性名要保持一致
//            BeanUtils.copyProperties(article,hotArticleVo);
//            hotArticleVoList.add(hotArticleVo);
//        }
        List<HotArticleVo> hotArticleVoList = BeanCopyUtils.copyListBean(articleList, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVoList);
    }

    /**
     * 查询所有的文章
     * @param pageNum 每页的文章数量
     * @param pageSize 总页数
     * @param categoryId 分类的id
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        //查询categoryName -> 通过categoryId查询category对象，获取categoryName，封装到Article对象categoryName中
        articles.stream()
                .map(article -> {
                    Long cateId = article.getCategoryId();
                    Category category = categoryService.getById(cateId);
                    if (category != null){
                        article.setCategoryName(category.getName());
                    }
//                    article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                    return article;
                }).collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyListBean(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        //封装查询结果
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据id获取文章信息
     * @param id 文章id
     * @return 文章详情
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //获取文章
        Article article = getById(id);
        // 从redis中获取浏览量
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 根据categoryId查询所属分类、封装到VO
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if (category != null){
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        // 添加博客
        save(article);
        //添加 博客和标签的关联
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticleList(ArticleListDto articleListDto) {
        Article article = BeanCopyUtils.copyBean(articleListDto, Article.class);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle,article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary,article.getSummary());
        Page<Article> page = new Page<>(articleListDto.getPageNum(),articleListDto.getPageSize());
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        List<ArticleLikeListVo> articleLikeListVos = BeanCopyUtils.copyListBean(articles, ArticleLikeListVo.class);
        PageVo pageVo = new PageVo(articleLikeListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult putArticle(ArticleByIdVo articleVo) {
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        update(article, queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        ArticleByIdVo articleByIdVo = BeanCopyUtils.copyBean(article, ArticleByIdVo.class);
        //根据分类id查询分类名
        Long categoryId = articleByIdVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleByIdVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleByIdVo);
    }
}
