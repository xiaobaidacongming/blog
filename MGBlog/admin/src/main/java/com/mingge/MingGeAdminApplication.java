package com.mingge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mingge.mapper")
public class MingGeAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MingGeAdminApplication.class,args);
    }
}
