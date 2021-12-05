package com.itblare.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "com.itblare.workflow.api",
        "com.itblare.workflow.service",
        "com.itblare.workflow.controller",
        "com.itblare.workflow.support.config",
        "com.itblare.workflow.support.wrapper",
        "com.itblare.workflow.support.spring",
        "com.itblare.workflow.support.handler",
        "com.itblare.workflow.support.security",
        "com.itblare.workflow.support.interceptor",
})
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

