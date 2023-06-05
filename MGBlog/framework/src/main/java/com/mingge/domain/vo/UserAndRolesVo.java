package com.mingge.domain.vo;

import com.mingge.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndRolesVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserByIdVo user;

}
