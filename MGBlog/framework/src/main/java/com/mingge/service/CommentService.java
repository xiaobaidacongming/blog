package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-10-03 20:55:24
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

