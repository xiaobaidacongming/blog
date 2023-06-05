package com.mingge.controller;

import com.mingge.annotation.SystemLog;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.User;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.service.UserService;
import com.mingge.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }
    @PutMapping("/userInfo")
    @SystemLog(businessName = "修改用户信息") // 指定业务名称
    public ResponseResult updateUserInfo(@RequestBody User user){
        // 确保修改用户信息是当前用户进行操作
        if (!SecurityUtils.getUserId().equals(user.getId())) return ResponseResult.errorResult(AppHttpCodeEnum.ILLEGAL_REQUEST);
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

}