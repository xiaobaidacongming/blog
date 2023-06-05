package com.mingge.domain.dto;

import lombok.Data;

@Data
public class CategoryListDto {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private String status;
}
