package com.mingge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingge.domain.entity.Menu;
import com.mingge.domain.vo.MenuTreeselectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-12 15:37:44
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(@Param("userId") Long userId,@Param("c")String c,@Param("f")String f);
    List<Menu> selectAllRouterMenu();
    List<MenuTreeselectVo> selectAllRouterMenuTreeselectVo();
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
    // 添加角色所关联的菜单权限
    Integer addRoleMenu(@Param("id") Long id,@Param("menuIds") List<Long> menuIds);
    Integer deleteRoleMenu(@Param("id") Long id);
    List<MenuTreeselectVo> roleMenuTreeselectByid(@Param("id") Long id);
}

