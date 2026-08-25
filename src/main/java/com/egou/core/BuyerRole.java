package com.egou.core;

import java.util.Arrays;
import java.util.List;

/**
 * 普通买家角色
 * 允许访问首页、购物车、订单、留言等功能
 * 禁止访问商家商品管理功能
 */
public class BuyerRole implements Role {

    /** 禁止访问的路径列表（商家专属功能） */
    private static final List<String> DENY_PATHS = Arrays.asList(
            "/com", "/add", "/up", "/show"
    );

    @Override
    public String getRoleName() {
        return "普通买家";
    }

    @Override
    public boolean allowAccess(String path) {
        if (path == null) {
            return false;
        }
        // 买家禁止访问商家功能路径
        return DENY_PATHS.stream().noneMatch(path::equals);
    }
}
