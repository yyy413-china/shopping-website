package com.egou.config;

import com.egou.core.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 注册权限拦截器
 * 静态资源由Spring Boot默认机制处理（classpath:/static/）
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 注册权限拦截器
     * 拦截所有请求，排除静态资源和部分公开接口
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/index", "/login", "/register", "/check", "/enrol", "/getpic",
                        "/403", "/css/**", "/js/**", "/images/**", "/upload/**",
                        "/webjars/**", "/favicon.ico"
                );
    }
}
