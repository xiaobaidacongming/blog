package com.mingge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.Menu;
import com.mingge.domain.vo.MenuById;
import com.mingge.domain.vo.MenuListVo;
import com.mingge.domain.vo.MenuRoleMenuTreeselectVo;
import com.mingge.domain.vo.MenuTreeselectVo;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import com.mingge.mapper.MenuMapper;
import com.mingge.service.MenuService;
import com.mingge.utils.BeanCopyUtils;
import com.mingge.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-12 15:37:47
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id,SystemConstants.MENU,SystemConstants.BUTTON);
    }

    @Override
    public List<String> selectPerms() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
        wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
        List<Menu> menus = list(wrapper);
        List<String> perms = menus.stream()
                .map(Menu::getPerms)
                .collect(Collectors.toList());
        return perms;
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,SystemConstants.MENU_PARENT_ID);
        return menuTree;
    }
    /**
     * 先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
     * @param menus 当前用户所有的菜单集合
     * @param parentId 父菜单的id
     * @return
     */
    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                // 设置当前元素的子元素的集合
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    /**
     * 获取当前menu的子menu的集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
    @Override
    public ResponseResult getMenus(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //针对菜单的状态进行查询
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        //针对菜单名进行模糊查询
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        //菜单要按照父菜单id和orderNum进行排序
        queryWrapper.orderBy(true,true,Menu::getParentId);
        queryWrapper.orderBy(true,true,Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        List<MenuListVo> menuListVos = BeanCopyUtils.copyListBean(list, MenuListVo.class);
        return ResponseResult.okResult(menuListVos);
    }

    @Override
    public ResponseResult getMenu(Long id) {
        if (id<0) throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        Menu menu = getById(id);
        MenuById menuById = BeanCopyUtils.copyBean(menu, MenuById.class);
        return ResponseResult.okResult(menuById);
    }

    @Override
    public ResponseResult putMuen(Menu menu) {
        if (menu.getId().equals(menu.getParentId())) throw new SystemException(AppHttpCodeEnum.MENU_PUT_ERROR);
        return updateById(menu)?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        //查询当前菜单
        Menu menu = getById(id);
        if (Objects.isNull(menu)) return ResponseResult.okResult();
        // 判断当前菜单是否有子菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menu.getId());
        Menu one = getOne(queryWrapper);
        // 存在子菜单不允许删除
        if (Objects.nonNull(one)) throw new SystemException(AppHttpCodeEnum.MENU_NOT_NULL);
        // 否则直接删除
        return removeById(id)?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
    }

    @Override
    public ResponseResult treeselect() {
        // 获取所有的菜单权限
        List<MenuTreeselectVo> menus = menuMapper.selectAllRouterMenuTreeselectVo();
        // 生成菜单树
        List<MenuTreeselectVo> menuList = buildMenuTree(menus, SystemConstants.MENU_PARENT_ID);
        return ResponseResult.okResult(menuList);
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        List<MenuTreeselectVo> menus;
        if (id == 1){
            menus = menuMapper.selectAllRouterMenuTreeselectVo();
        }else {
            menus=menuMapper.roleMenuTreeselectByid(id);
        }
        // 生成菜单树
        List<MenuTreeselectVo> menuList = buildMenuTree(menus, SystemConstants.MENU_PARENT_ID);
        return ResponseResult.okResult(new MenuRoleMenuTreeselectVo(menuList));
    }

    /**
     * 获取菜单树
     * @param menus 所有菜单
     * @param parientId 父菜单id
     */
    private List<MenuTreeselectVo> buildMenuTree(List<MenuTreeselectVo> menus,Long parientId){
        List<MenuTreeselectVo> menuList = menus.stream()
                .filter(menu -> menu.getParentId().equals(parientId))
                // 设置当前元素的子元素的集合
                .map(menu -> menu.setChildren(getChildrens(menu, menus)))
                .collect(Collectors.toList());
        return menuList;
    }
    // 设置menu的子菜单
    private List<MenuTreeselectVo> getChildrens(MenuTreeselectVo menu, List<MenuTreeselectVo> menus) {
        List<MenuTreeselectVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                // 递归赋值  未生效
                .map(m -> m.setChildren(getChildrens(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}

