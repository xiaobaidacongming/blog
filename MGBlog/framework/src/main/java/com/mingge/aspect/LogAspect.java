package com.mingge.aspect;

import com.alibaba.fastjson.JSON;
import com.mingge.annotation.SystemLog;
import com.mingge.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {
    // 切点   指定注解进行标识
    @Pointcut("@annotation(com.mingge.annotation.SystemLog)")
    public void pt(){
    }

    // 控制方法
    @Around("pt()")         // 被增强方法的信息
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            // 前置通知
            handleBefore(joinPoint);
            result = joinPoint.proceed();
            handleAfter(result);
            // 后置通知
        } finally { // 这里不同catch，否则统一异常处理器就会失效
            // 最终通知
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator()); // 系统换行符
        }
        return result;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        // 获取当先线程中的请求对象   RequestContextHolder用threadlocal进行数据共享，保证多线程资源的隔离
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        // 在实现类中有request请求对象     request报文封装成request对象
        HttpServletRequest request = requestAttributes.getRequest();
        // 被增强方法的签名
        Signature signature = joinPoint.getSignature();
        // 被增强方法上的SystemLog注解
        SystemLog systemLog = getSystemlog(signature);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印接口的描述信息
        log.info("BusinessName   : {}",systemLog.businessName()); // 如何获取代理对象上的注解？
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", signature.getDeclaringTypeName(),signature.getName());//((MethodSignature)signature)
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private SystemLog getSystemlog(Signature signature) {
        // 增强方法的签名（signature）就是被增强方法，包括注解、方法名、方法体
        MethodSignature methodSignature = (MethodSignature)signature;
        // 被增强的方法
        Method method = methodSignature.getMethod();
        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        return systemLog;
    }

    private void handleAfter(Object result) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(result));
    }

}
