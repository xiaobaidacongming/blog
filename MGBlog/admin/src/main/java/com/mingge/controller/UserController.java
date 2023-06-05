package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.UserAddDto;
import com.mingge.domain.dto.UserDto;
import com.mingge.domain.dto.UserPutDto;
import com.mingge.domain.dto.UserStatusDto;
import com.mingge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult listUser(UserDto userDto){
        return userService.getUsers(userDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getUser(@PathVariable("id")Long id){
        return userService.getUser(id);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody UserAddDto userDto){
        return userService.addUser(userDto);
    }
    @PutMapping
    public ResponseResult modifyUser(@RequestBody UserPutDto userDto){
        return userService.modifyUser(userDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult modifyUserStatus(@RequestBody UserStatusDto userStatusDto){
        return userService.modifyUserStatus(userStatusDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id")Long id){
        return userService.deleteUser(id);
    }
}
