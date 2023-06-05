package com.mingge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mingge.controller"))
                .build();
    }

    private ApiInfo apiInfo() {     // 团队名          团队网址             邮箱信息
        Contact contact = new Contact("龙华", "http://www.my.com", "2049162191@qq.com");
        return new ApiInfoBuilder()
                .title("明哥博客系统") // 文档标题
                .description("前台系统") // 文档描述
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}