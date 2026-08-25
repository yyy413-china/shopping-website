package com.egou.config;

import com.egou.domain.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Date;

/**
 * Spring AOP切面配置类
 * 拦截所有Controller访问请求
 * 用户访问任意页面时，自动刷新在线状态(onlineyes)和最后登录时间(logintime)
 * 实现活跃用户统计功能，代码无侵入
 */
@Aspect
@Component
public class AopConfig {

    /**
     * 定义切点：拦截所有Controller包下的方法
     */
    @Pointcut("execution(* com.egou.controller.*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 后置通知：Controller方法执行后，更新用户在线状态和登录时间
     */
    @AfterReturning("controllerPointcut()")
    public void afterControllerMethod(JoinPoint joinPoint) {
        try {
            // 获取当前请求的Session
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpSession session = attributes.getRequest().getSession(false);
                if (session != null) {
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                        // 更新在线状态为在线
                        user.setOnlineyes(1);
                        // 更新最后登录时间
                        user.setLogintime(new Date());
                        // 将更新后的用户信息存回Session
                        session.setAttribute("user", user);
                    }
                }
            }
        } catch (Exception e) {
            // AOP切面异常不影响主业务流程
            e.printStackTrace();
        }
    }
}
