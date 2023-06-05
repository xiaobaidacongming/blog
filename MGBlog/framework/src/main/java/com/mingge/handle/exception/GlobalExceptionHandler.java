package com.mingge.handle.exception;

import com.mingge.domain.ResponseResult;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
// 对controller进行增强，controller有异常都会在这里进行统一的处理
@RestControllerAdvice //这些方法的返回值都会封装在响应体中，返回给前端
@Slf4j
public class GlobalExceptionHandler {
//因为他自定义的登录接口，没走springsecurity的过滤器，所以密码错误是全局异常处理器捕获的，
// 由exceptionHandler方法处理，响应的状态码是500
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
