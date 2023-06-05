package com.mingge.handle.security;

import com.alibaba.fastjson.JSON;
import com.mingge.domain.ResponseResult;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component   // 直译：拒绝访问处理器
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override//授权失败的处理器
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
