package com.mingge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.Article;
import com.mingge.domain.entity.Comment;
import com.mingge.domain.vo.CommentVo;
import com.mingge.domain.vo.PageVo;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import com.mingge.mapper.ArticleMapper;
import com.mingge.mapper.CommentMapper;
import com.mingge.service.CommentService;
import com.mingge.service.UserService;
import com.mingge.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-10-03 20:55:25
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleMapper articleMapper;
    @Override //查询文章的根评论
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
       //查询当前文章
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //当前文章的根评论 rootId=-1
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT_ID);
        //评论类型
        queryWrapper.eq(Comment::getType,commentType);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        Page<Comment> page = new Page<>(pageNum,pageSize);
        //调用怕个方法查询到的数据也就保存到page对象中
        page(page,queryWrapper);
        //获取commentList数据
        List<CommentVo> commentVoList =toCommentVoList(page.getRecords());
        //查询对应根评论的子评论，并赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }
        //分页查询
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        Article article = articleMapper.selectById(comment.getArticleId());
        if (article == null) throw new SystemException(AppHttpCodeEnum.ERROR);
        if (article.getIsComment().equals(SystemConstants.ARTICLE_IS_COMMENT)){
            throw new SystemException(AppHttpCodeEnum.ARTICLE_COMMENT_NOT);
        }
        save(comment);//插入方法
        return ResponseResult.okResult();
    }

    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyListBean(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if(commentVo.getToCommentUserId()!= -1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }

    /**
     * 根据跟评论的id查询所对应子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }
}

