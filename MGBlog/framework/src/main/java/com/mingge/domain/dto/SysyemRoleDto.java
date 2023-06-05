package com.mingge.domain.dto;

import lombok.Data;

import java.util.List;
@Data
public class SysyemRoleDto {
    // 角色名称
    private String roleName;
    // 权限字符
    private String roleKey;
    // 显示顺序
    private Integer roleSort;
    private String status;
    private List<Long> menuIds;
    private String remark;
}
