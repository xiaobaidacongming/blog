package com.mingge.domain.dto;

import lombok.Data;

@Data
public class RoleDto {
    private Integer pageNum;
    private Integer pageSize;
    private String roleName;
    // 角色状态
    private String status;
}
