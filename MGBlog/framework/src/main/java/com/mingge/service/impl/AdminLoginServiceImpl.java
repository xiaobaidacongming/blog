package com.mingge.service.impl;

import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.LoginUser;
import com.mingge.domain.entity.User;
import com.mingge.domain.vo.BlogUserLoginVo;
import com.mingge.domain.vo.UserInfoVo;
import com.mingge.service.AdminLoginService;
import com.mingge.utils.BeanCopyUtils;
import com.mingge.utils.JwtUtil;
import com.mingge.utils.RedisCache;
import com.mingge.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.ADMIN_LOGIN+userId,loginUser);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        // 从redis中删除用户信息
        redisCache.deleteObject(SystemConstants.ADMIN_LOGIN+userId);
        return ResponseResult.okResult();
    }
}
