package com.egou.core;

import com.egou.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 登录权限拦截器
 * 拦截请求，校验用户角色权限
 * 游客禁止访问购物车/商品管理，买家禁止访问商家功能，商家禁止访问购物车
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        // 获取Session中的用户信息
        User user = (User) session.getAttribute("user");
        // 获取请求路径
        String path = request.getRequestURI();
        // 去掉项目上下文路径
        String contextPath = request.getContextPath();
        if (contextPath != null && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        // 获取用户角色
        Integer roleValue = (user != null) ? user.getRole() : null;
        Role role = RoleFactory.getRole(roleValue);

        // 校验权限
        if (!role.allowAccess(path)) {
            // 无权限，重定向到403页面
            response.sendRedirect(contextPath + "/403");
            return false;
        }

        return true;
    }
}
