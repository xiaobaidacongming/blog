package com.mingge.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLikeListVo {
    //所属分类id
    private Long categoryId;
    //文章内容
    private String content;
    private Date createTime;
    private Integer delFlag;
    private Long id;
    //是否允许评论 1是，0否
    private String isComment;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //文章摘要
    private String summary;
    //缩略图
    private String thumbnail;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
