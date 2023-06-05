package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.RoleDto;
import com.mingge.domain.dto.SysyemRoleChilirenDto;
import com.mingge.domain.dto.SysyemRoleDto;
import com.mingge.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-05-12 15:46:58
 */
public interface RoleService extends IService<Role> {
    /**
     * 根据用户id查询角色信息
     * @param id
     * @return
     */
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getRoles(RoleDto roleDto);

    ResponseResult changeStatus(Long roleId, String status);

    ResponseResult addRole(SysyemRoleDto roleDto);

    ResponseResult getRole(Long id);

    ResponseResult putRole(SysyemRoleChilirenDto role);

    ResponseResult listAllRole();
}

