package com.egou.core;

/**
 * 角色接口
 * 利用Java多态实现不同角色的权限控制
 */
public interface Role {
    /**
     * 获取角色名称
     */
    String getRoleName();

    /**
     * 判断是否允许访问指定路径
     * @param path 请求路径
     * @return true-允许，false-禁止
     */
    boolean allowAccess(String path);
}
