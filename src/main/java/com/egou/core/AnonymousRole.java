package com.egou.core;

import java.util.Arrays;
import java.util.List;

/**
 * 匿名游客角色
 * 允许访问首页、登录、注册、商品详情页
 * 禁止访问购物车、商品管理、个人主页等功能
 */
public class AnonymousRole implements Role {

    /** 允许访问的路径列表 */
    private static final List<String> ALLOW_PATHS = Arrays.asList(
            "/", "/index", "/login", "/register", "/check", "/enrol", "/getpic", "/403",
            "/checkLogin", "/toggleFavorite", "/buyNow", "/profile", "/updateProfile",
            "/dashboard", "/dashboard/stats"
    );

    @Override
    public String getRoleName() {
        return "匿名游客";
    }

    @Override
    public boolean allowAccess(String path) {
        if (path == null) {
            return false;
        }
        // 游客允许访问白名单路径
        if (ALLOW_PATHS.stream().anyMatch(path::equals)) {
            return true;
        }
        // 游客允许访问商品详情页 /product/{id}
        if (path.startsWith("/product/")) {
            return true;
        }
        return false;
    }
}
