package com.egou.service.impl;

import com.egou.domain.User;
import com.egou.mapper.UserMapper;
import com.egou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现类
 * 处理用户登录、注册、状态更新等业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * 校验账号密码，更新在线状态和登录时间
     */
    @Override
    public User login(String account, String password) {
        if (account == null || password == null) {
            return null;
        }
        User user = userMapper.findByAccount(account);
        if (user != null && user.getPassword().equals(password)) {
            // 登录成功，更新在线状态和登录时间
            userMapper.updateOnlineStatus(user.getId(), 1);
            userMapper.updateLoginTime(user.getId());
            user.setOnlineyes(1);
            return user;
        }
        return null;
    }

    /**
     * 用户注册
     * 校验账号是否已存在，设置默认值
     */
    @Override
    public boolean register(User user) {
        if (user == null || user.getAccount() == null || user.getPassword() == null) {
            return false;
        }
        // 检查账号是否已存在
        User existUser = userMapper.findByAccount(user.getAccount());
        if (existUser != null) {
            return false;
        }
        // 设置默认值
        user.setOnlineyes(0);
        if (user.getRole() == null) {
            user.setRole(0); // 默认为普通买家
        }
        return userMapper.insert(user) > 0;
    }

    /**
     * 根据ID查询用户
     */
    @Override
    public User findById(Integer id) {
        if (id == null) {
            return null;
        }
        return userMapper.findById(id);
    }

    /**
     * 更新用户在线状态
     */
    @Override
    public void updateOnlineStatus(Integer id, Integer onlineyes) {
        if (id != null) {
            userMapper.updateOnlineStatus(id, onlineyes);
        }
    }
}
