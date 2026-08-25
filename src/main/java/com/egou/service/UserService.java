package com.egou.service;

import com.egou.domain.User;

/**
 * 用户业务接口
 */
public interface UserService {

    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     * @return 用户对象，登录失败返回null
     */
    User login(String account, String password);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    boolean register(User user);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User findById(Integer id);

    /**
     * 更新用户在线状态
     * @param id 用户ID
     * @param onlineyes 在线状态
     */
    void updateOnlineStatus(Integer id, Integer onlineyes);
}
