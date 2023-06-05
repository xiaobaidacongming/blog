package com.mingge.domain.vo;

import com.mingge.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleByIdVo extends ArticleLikeListVo{
    private Long createBy;
    private List<Long> tags;
    private String categoryName;
    private Long updateBy;
    private Date updateTime;
 }
