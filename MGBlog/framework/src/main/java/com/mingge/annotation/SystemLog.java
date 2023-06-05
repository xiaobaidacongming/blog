package com.mingge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME) // 运行时
@Target(ElementType.METHOD) // 作用在方法上
public @interface SystemLog {
    String businessName(); // 业务的名字
}
