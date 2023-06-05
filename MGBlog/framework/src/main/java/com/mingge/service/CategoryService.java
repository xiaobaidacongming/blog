package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.CategoryAddVo;
import com.mingge.domain.dto.CategoryListDto;
import com.mingge.domain.dto.CategoryModifyDto;
import com.mingge.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-10-01 19:11:09
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listCategory(CategoryListDto categoryListDto);

    ResponseResult addCategory(CategoryAddVo categoryVo);

    ResponseResult getCategory(Long id);

    ResponseResult modify(CategoryModifyDto categoryVo);
}

