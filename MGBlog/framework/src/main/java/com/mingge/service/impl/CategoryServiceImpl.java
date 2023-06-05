package com.mingge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.CategoryAddVo;
import com.mingge.domain.dto.CategoryListDto;
import com.mingge.domain.dto.CategoryModifyDto;
import com.mingge.domain.entity.Article;
import com.mingge.domain.entity.Category;
import com.mingge.domain.vo.CategoryListVo;
import com.mingge.domain.vo.CategoryVo;
import com.mingge.domain.vo.GetCategoryVo;
import com.mingge.domain.vo.PageVo;
import com.mingge.mapper.CategoryMapper;
import com.mingge.service.ArticleService;
import com.mingge.service.CategoryService;
import com.mingge.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-10-01 19:11:10
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        // 查询文章 状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的id 并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        // 查询分类表
        List<Category> categories = listByIds(categoryIds);
        //去重 可在查询的时候传条件比较器、也可以都查出来过滤
        categories = categories.stream() // 返回值为true的保留下来
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyListBean(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        // 获取所有的分类
        List<GetCategoryVo> categoryVoList = categoryMapper.getAllCategoryVo();
        return ResponseResult.okResult(categoryVoList);
    }

    @Override
    public ResponseResult listCategory(CategoryListDto categoryListDto) {
        PageHelper.startPage(categoryListDto.getPageNum(),categoryListDto.getPageSize());
        List<CategoryListVo> categorysVo=categoryMapper.selectAllCategory(categoryListDto);
        PageInfo<CategoryListVo> pageInfo = new PageInfo<>(categorysVo);
        return ResponseResult.okResult(new PageVo(pageInfo.getList(),pageInfo.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryAddVo categoryVo) {
        Category category = BeanCopyUtils.copyBean(categoryVo, Category.class);
        categoryMapper.insert(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategory(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryListVo categoryVo = BeanCopyUtils.copyBean(category, CategoryListVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult modify(CategoryModifyDto categoryVo) {
        Category category = BeanCopyUtils.copyBean(categoryVo, Category.class);
        categoryMapper.updateById(category);
        return ResponseResult.okResult();
    }
}

