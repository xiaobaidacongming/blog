package com.mingge.service;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
