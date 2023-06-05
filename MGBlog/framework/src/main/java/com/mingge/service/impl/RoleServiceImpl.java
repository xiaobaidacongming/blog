package com.mingge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.RoleDto;
import com.mingge.domain.dto.SysyemRoleChilirenDto;
import com.mingge.domain.dto.SysyemRoleDto;
import com.mingge.domain.entity.Role;
import com.mingge.domain.vo.PageVo;
import com.mingge.domain.vo.RoleChildrenVo;
import com.mingge.domain.vo.RoleVo;
import com.mingge.domain.vo.RoleVo1;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import com.mingge.mapper.MenuMapper;
import com.mingge.mapper.RoleMapper;
import com.mingge.service.RoleService;
import com.mingge.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-05-12 15:46:58
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是超级管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            // admin
            roleKeys.add(SystemConstants.SYSTEM_ADMIN_ROLE);
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getRoles(RoleDto roleDto) {
        PageHelper.startPage(roleDto.getPageNum(),roleDto.getPageSize());
        List<RoleVo> roleVos = roleMapper.selectAllByNameOrStatus(roleDto.getRoleName(), roleDto.getStatus());
        PageInfo<RoleVo> pageInfo = new PageInfo<>(roleVos);
        PageVo pageVo = new PageVo(pageInfo.getList(),pageInfo.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(Long roleId, String status) {
        if (!StringUtils.hasText(roleId+"")||!StringUtils.hasText(status)){
            throw new SystemException(AppHttpCodeEnum.ERROR);
        }
        roleMapper.updateStatusById(roleId,status);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(SysyemRoleDto roleDto) {
        // 添加角色
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        roleMapper.insert(role);
        //添加角色所关联的菜单权限
        List<Long> menuIds = roleDto.getMenuIds();
        if (menuIds != null || menuIds.size()>0){
            menuMapper.addRoleMenu(role.getId(),roleDto.getMenuIds());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = roleMapper.selectById(id);
        RoleVo1 vo = BeanCopyUtils.copyBean(role, RoleVo1.class);
        return ResponseResult.okResult(vo);
    }

    @Transactional
    @Override
    public ResponseResult putRole(SysyemRoleChilirenDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        // 更新角色
        roleMapper.updateById(role);
        // 清空权限
        menuMapper.deleteRoleMenu(role.getId());
        //添加角色所关联的菜单权限
        List<Long> menuIds = roleDto.getMenuIds();
        if (menuIds != null || menuIds.size()>0){
            menuMapper.addRoleMenu(role.getId(),roleDto.getMenuIds());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> roles = roleMapper.selectList(null);
        List<RoleChildrenVo> roleChildrenVos = BeanCopyUtils.copyListBean(roles, RoleChildrenVo.class);
        return ResponseResult.okResult(roleChildrenVos);
    }
}

