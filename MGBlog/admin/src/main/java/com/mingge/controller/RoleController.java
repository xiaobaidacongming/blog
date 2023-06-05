package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.RoleDto;
import com.mingge.domain.dto.RoleDto1;
import com.mingge.domain.dto.SysyemRoleChilirenDto;
import com.mingge.domain.dto.SysyemRoleDto;
import com.mingge.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult listRole(RoleDto roleDto){
        return roleService.getRoles(roleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable("id") Long id){
        return roleService.getRole(id);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
    @PutMapping("/changeStatus")
    //在同一个@RequestBody注解下使用了两个参数，而@RequestBody注解只能用于单个参数。这导致了fastjson在尝试反序列化JSON字符串时出现类型转换错误。
//    public ResponseResult changeStatus(@RequestBody Long roleId,@RequestBody String status){
    public ResponseResult changeStatus(@RequestBody RoleDto1 role){
        return roleService.changeStatus(role.getRoleId(), role.getStatus());
    }
    @PutMapping
    public ResponseResult putRole(@RequestBody SysyemRoleChilirenDto role){
        return roleService.putRole(role);
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody SysyemRoleDto roleDto){
        return roleService.addRole(roleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id")Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }
}
