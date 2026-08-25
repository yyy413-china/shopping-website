package com.egou.core;

/**
 * 角色工厂类
 * 根据用户角色值创建对应的角色实现类
 * 利用Java多态实现角色权限控制
 */
public class RoleFactory {

    /**
     * 根据角色值获取角色对象
     * @param roleValue 角色值：null/其他-游客，0-买家，1-商家
     * @return 角色实现类
     */
    public static Role getRole(Integer roleValue) {
        if (roleValue == null) {
            return new AnonymousRole();
        }
        switch (roleValue) {
            case 0:
                return new BuyerRole();
            case 1:
                return new SupplierRole();
            default:
                return new AnonymousRole();
        }
    }
}
