package com.mingge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.dto.UserAddDto;
import com.mingge.domain.dto.UserDto;
import com.mingge.domain.dto.UserPutDto;
import com.mingge.domain.dto.UserStatusDto;
import com.mingge.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-10-03 22:11:57
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUsers(UserDto userDto);

    ResponseResult addUser(UserAddDto userDto);

    ResponseResult deleteUser(Long id);

    ResponseResult getUser(Long id);

    ResponseResult modifyUser(UserPutDto userDto);

    ResponseResult modifyUserStatus(UserStatusDto userStatusDto);
}

