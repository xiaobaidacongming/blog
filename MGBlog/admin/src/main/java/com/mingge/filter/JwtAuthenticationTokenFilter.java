package com.mingge.filter;

import com.alibaba.fastjson.JSON;
import com.mingge.constants.SystemConstants;
import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.LoginUser;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.utils.JwtUtil;
import com.mingge.utils.RedisCache;
import com.mingge.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录  直接放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时  token非法
            //响应告诉前端需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //在过滤器中把Json串返回到响应体当中
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject(SystemConstants.ADMIN_LOGIN + userId);
        //如果获取不到
        if(Objects.isNull(loginUser)){
            //说明登录过期  提示重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //登录的时候把loginUser 存入SecurityContextHolder 供退出时使用
        UsernamePasswordAuthenticationToken authenticationToken =
                // 这里是 new 的对象，每个用户的信息都存在自己的线程中
                // 未认证状态下的用两个参数，认证状态下的用三个参数
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        //多个请求进来，数据不会乱掉，因为每一个线程会有自己的数据，  SecurityContextHolder底层用的是ThreadLocal，
        // 多个请求进来，数据不会乱掉，不会有并发的问题，所有的用户信息都会存在线程数组中，取值的时候直接取，就会获取到自己的用户信息
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }


}