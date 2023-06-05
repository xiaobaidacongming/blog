package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.entity.Role;
import com.mingge.domain.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-12 15:46:58
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(@Param("userId") Long userId);

    List<RoleVo> selectAllByNameOrStatus(@Param("roleName") String roleName, @Param("status") String status);

    Integer updateStatusById(@Param("roleId")Long roleId,@Param("status") String status);
}

