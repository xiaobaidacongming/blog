package com.mingge.domain.dto;

import lombok.Data;

@Data
public class ArticleListDto {
    // 页码
    private Integer pageNum;
    // 每页条数
    private Integer pageSize;
    // 文章标题
    private String title;
    // 文章摘要
    private String summary;
}
