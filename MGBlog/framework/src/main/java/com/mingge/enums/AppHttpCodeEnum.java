package com.mingge.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    CODE_EXIST(200,"验证码已存在，还没有过期"),

    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    ERROR(502,"操作失败"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506,"评论内容不能为空"),
    FILE_TYPE_ERROR(507,"文件类型错误，请传输图片文件"),
    UPDATE_USER_INFO_ERROR(508,"更新用户信息失败"),
    ILLEGAL_REQUEST(509,"非法请求"),
    USERNAME_NOT_NULL(510,"必须填写用户名"),
    PASSWORD_NOT_NULL(511,"必须填写密码"),
    EMAIL_NOT_NULL(512,"邮箱不能为空"),
    NICKNAME_NOT_NULL(513,"昵称不能为空"),
    PHONE_ILLEGAL(515,"电话号码不合规"),
    NICKNAME_EXIST(514, "昵称已存在"),
    TAG_NAME_NULL(515,"标签名不能为空"),
    ID_NULL(516,"id不能为空"),
    TAG_NULL(517,"该标签不存在"),
    UPLOAD_IMG_ERROR(518,"上传图片失败"),
    MENU_PUT_ERROR(500,"修改菜单'写博文'失败，上级菜单不能选择自己"),
    MENU_NOT_NULL(500,"存在子菜单不允许删除"),
    PHONE_EXIST(519,"手机号已存在"),
    ARTICLE_COMMENT_NOT(520,"当前文章不允许评论");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}