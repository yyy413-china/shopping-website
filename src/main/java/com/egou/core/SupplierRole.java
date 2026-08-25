package com.egou.core;

import java.util.Arrays;
import java.util.List;

/**
 * 商家角色
 * 允许访问首页、商品管理、买家留言等功能
 * 禁止访问购物车功能
 */
public class SupplierRole implements Role {

    /** 禁止访问的路径列表（买家专属功能） */
    private static final List<String> DENY_PATHS = Arrays.asList(
            "/cart", "/addtocart", "/changecnum", "/pay", "/changeconfirm"
    );

    @Override
    public String getRoleName() {
        return "商家";
    }

    @Override
    public boolean allowAccess(String path) {
        if (path == null) {
            return false;
        }
        // 商家禁止访问购物车功能路径
        return DENY_PATHS.stream().noneMatch(path::equals);
    }
}
