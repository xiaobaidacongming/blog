package com.mingge.domain.vo;

import lombok.Data;

@Data
public class UserByIdVo {
    private String email;
    private Long id;
    private String nickName;
    private String sex;
    private String status;
    private String userName;
}
