package com.egou.controller;

import com.egou.domain.Result;
import com.egou.domain.User;
import com.egou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

/**
 * 用户控制器
 * 对接接口：/login、/check、/register、/enrol、/logout
 * 功能：用户登录、注册、注销
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 跳转登录页面
     * 请求方式：GET
     * 请求路径：/login
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 用户登录校验
     * 请求方式：POST
     * 请求路径：/check
     * 参数：account（账号）、password（密码）
     * 返回值：统一返回结果
     */
    @PostMapping("/check")
    @ResponseBody
    public Result<Object> check(@RequestParam String account, @RequestParam String password, HttpSession session) {
        if (account == null || account.trim().isEmpty()) {
            return Result.fail("账号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.fail("密码不能为空");
        }
        User user = userService.login(account, password);
        if (user != null) {
            // 登录成功，将用户信息和角色存入Session
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            return Result.success("登录成功");
        }
        return Result.fail("账号或密码错误");
    }

    /**
     * 跳转注册页面
     * 请求方式：GET
     * 请求路径：/register
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * 用户注册
     * 请求方式：POST
     * 请求路径：/enrol
     * 参数：account、password、name、sex、idcard、phone、role
     * 返回值：统一返回结果
     */
    @PostMapping("/enrol")
    @ResponseBody
    public Result<Object> enrol(User user) {
        if (user == null || user.getAccount() == null || user.getAccount().trim().isEmpty()) {
            return Result.fail("账号不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return Result.fail("密码不能为空");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return Result.fail("姓名不能为空");
        }
        boolean result = userService.register(user);
        if (result) {
            return Result.success("注册成功");
        }
        return Result.fail("注册失败，账号可能已存在");
    }

    /**
     * 用户注销
     * 请求方式：GET
     * 请求路径：/logout
     * 功能：清空Session，更新在线状态为离线
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // 更新在线状态为离线
            userService.updateOnlineStatus(user.getId(), 0);
        }
        // 清空Session
        session.invalidate();
        return "redirect:/index";
    }
}
