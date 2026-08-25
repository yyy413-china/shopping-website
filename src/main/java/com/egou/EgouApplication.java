package com.egou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * E购商城启动类
 */
@SpringBootApplication
@MapperScan("com.egou.mapper")
@EnableScheduling
public class EgouApplication {
    public static void main(String[] args) {
        SpringApplication.run(EgouApplication.class, args);
    }
}
