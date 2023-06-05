package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-05-12 15:37:46
 */
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);
    List<String> selectPerms();
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult getMenus(String status, String menuName);

    ResponseResult getMenu(Long id);

    ResponseResult putMuen(Menu menu);

    ResponseResult deleteMenu(Long id);

    ResponseResult treeselect();

    ResponseResult roleMenuTreeselect(Long id);
}

