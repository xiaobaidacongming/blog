package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.dto.CategoryListDto;
import com.mingge.domain.entity.Category;
import com.mingge.domain.vo.CategoryListVo;
import com.mingge.domain.vo.GetCategoryVo;

import java.util.List;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-01 19:11:08
 */
public interface CategoryMapper extends BaseMapper<Category> {
    List<GetCategoryVo> getAllCategoryVo();

    List<CategoryListVo> selectAllCategory(CategoryListDto categoryListDto);
}

