package com.mingge.domain.dto;

import lombok.Data;

import java.util.List;
@Data
public class UserAddDto {
    private String userName;
    //昵称
    private String nickName;
    //密码
    private String password;
    private String email;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;
    private String status;
    // 一个用户可能具备多种角色
    private List<Long> roleIds;
}
