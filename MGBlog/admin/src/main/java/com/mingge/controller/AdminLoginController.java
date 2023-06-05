package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.AdminLoginDto;
import com.mingge.domain.entity.LoginUser;
import com.mingge.domain.entity.Menu;
import com.mingge.domain.entity.User;
import com.mingge.domain.vo.AdminUserInfoVo;
import com.mingge.domain.vo.RoutersVo;
import com.mingge.domain.vo.UserInfoVo;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import com.mingge.service.AdminLoginService;
import com.mingge.service.MenuService;
import com.mingge.service.RoleService;
import com.mingge.utils.BeanCopyUtils;
import com.mingge.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult adminLong(@RequestBody AdminLoginDto adminLoginDto){
        if (!StringUtils.hasText(adminLoginDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        User user = BeanCopyUtils.copyBean(adminLoginDto, User.class);
        return adminLoginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult adminLogout(){
        System.out.println("退出登录==========");
        return adminLoginService.logout();
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

}
