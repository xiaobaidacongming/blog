package com.mingge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling // 定时任务注解
@MapperScan("com.mingge.mapper")
@EnableSwagger2
public class MingGeBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MingGeBlogApplication.class);
    }
}
