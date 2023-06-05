package com.mingge.domain.vo;

import lombok.Data;

@Data
public class CategoryListVo {
    private Long id;
    //分类名
    private String name;
    private String status;
    private String description;

}
