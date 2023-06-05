package com.mingge.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPutDto {
    private Long id;
    private String email;
    private String nickName;
    private String sex;
    private String userName;
    //账号状态（0正常 1停用）
    private String status;
    private List<Long> roleIds;
}
