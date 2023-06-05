package com.mingge.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.CategoryAddVo;
import com.mingge.domain.dto.CategoryListDto;
import com.mingge.domain.dto.CategoryModifyDto;
import com.mingge.domain.entity.Category;
import com.mingge.domain.vo.ExcelCategoryVo;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.service.CategoryService;
import com.mingge.utils.BeanCopyUtils;
import com.mingge.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult  listAllCategory(){
        return categoryService.listAllCategory();
    }
    @GetMapping("/export") // 从容器中获取ps的bean，调用hasPermission方法
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyListBean(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult list(CategoryListDto categoryListDto){
        return categoryService.listCategory(categoryListDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategory(@PathVariable("id") Long id){
        return categoryService.getCategory(id);
    }
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryAddVo categoryVo){
        return categoryService.addCategory(categoryVo);
    }
    @PutMapping
    public ResponseResult modifyCategory(@RequestBody CategoryModifyDto categoryVo){
        return categoryService.modify(categoryVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id")Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }
}
