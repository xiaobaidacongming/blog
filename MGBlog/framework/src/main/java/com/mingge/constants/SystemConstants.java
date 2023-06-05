package com.mingge.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    public static final String STATUS_NORMAL = "0";
    /**
     * 友链审核已通过的
     */
    public static final String Link_STATUS_APPROVED = "0";
    /**
     * 把信息存储到redis中的前缀
     */
    public static final String BLOG_LOGIN = "bloglogin:";
    public static final String ADMIN_LOGIN = "adminlogin:";

    public static final Integer COMMENT_ROOT_ID = -1;
    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 文章不允许评论
     */
    public static final String ARTICLE_IS_COMMENT = "0";

    /**
     * 评论类型为：友链评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     * 文章浏览量
     */
    public static final String ARTICLE_VIEW_COUNT = "article:viewCount";
    /**
     * 系统管理员角色
     */
    public static final String SYSTEM_ADMIN_ROLE = "admin";
    /**
     * 菜单
     */
    public static final String MENU = "C";
    /**
     * 按钮
     */
    public static final String BUTTON = "F";
    /**
     * 注册手机号存储的短信验证码
     */
    public static final String REGISTER_PHONE = "register:";
    /**
     * 管理员
     */
    public static final String ADMIN = "1";
    /**
     * 根菜单的id
     */
    public static final Long MENU_PARENT_ID = 0L;
}