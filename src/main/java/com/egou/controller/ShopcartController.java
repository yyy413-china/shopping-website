package com.egou.controller;

import com.egou.domain.Result;
import com.egou.domain.Shopcart;
import com.egou.domain.User;
import com.egou.service.ShopcartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * 购物车控制器
 * 对接接口：/cart、/addtocart、/changecnum、/pay、/changeconfirm
 * 功能：商品加入购物车、修改数量、删除商品、订单结算
 * 权限：仅买家可访问
 */
@Controller
public class ShopcartController {

    @Autowired
    private ShopcartService shopcartService;

    /**
     * 购物车页面
     * 请求方式：GET
     * 请求路径：/cart
     * 功能：展示未结算购物车和已结算订单
     */
    @GetMapping("/cart")
    public String cartPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // 查询未结算购物车
        List<Shopcart> unpaidList = shopcartService.findUnpaidByUserId(user.getId());
        model.addAttribute("unpaidList", unpaidList);
        // 查询已结算订单
        List<Shopcart> paidList = shopcartService.findPaidByUserId(user.getId());
        model.addAttribute("paidList", paidList);
        model.addAttribute("user", user);
        return "cart";
    }

    /**
     * 加入购物车
     * 请求方式：GET
     * 请求路径：/addtocart
     * 参数：commodityid（商品ID）
     * 返回值：统一返回结果
     */
    @GetMapping("/addtocart")
    @ResponseBody
    public Result<Object> addToCart(@RequestParam Integer commodityid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(401, "请先登录", null);
        }
        if (commodityid == null) {
            return Result.fail("商品ID不能为空");
        }
        boolean result = shopcartService.addToCart(user.getId(), commodityid);
        if (result) {
            return Result.success("已加入购物车");
        }
        return Result.fail("加入购物车失败");
    }

    /**
     * 修改购物车商品数量
     * 请求方式：POST
     * 请求路径：/changecnum
     * 参数：id（购物车ID）、cnum（新数量）
     */
    @PostMapping("/changecnum")
    @ResponseBody
    public Result<Object> changeCnum(@RequestParam Integer id, @RequestParam Integer cnum) {
        if (id == null) {
            return Result.fail("购物车ID不能为空");
        }
        if (cnum == null || cnum <= 0) {
            return Result.fail("数量必须大于0");
        }
        boolean result = shopcartService.changeNum(id, cnum);
        if (result) {
            return Result.success("修改数量成功");
        }
        return Result.fail("修改数量失败");
    }

    /**
     * 订单结算
     * 请求方式：POST
     * 请求路径：/pay
     * 功能：生成订单、扣减库存、更新购物车状态
     */
    @PostMapping("/pay")
    @ResponseBody
    public Result<Object> pay(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(403, "请先登录", null);
        }
        String orderNo = shopcartService.pay(user.getId());
        if (orderNo != null) {
            return Result.success("结算成功，订单号：" + orderNo);
        }
        return Result.fail("结算失败，购物车为空");
    }

    /**
     * 删除购物车商品
     * 请求方式：GET
     * 请求路径：/changeconfirm
     * 参数：id（购物车ID）
     */
    @GetMapping("/changeconfirm")
    public String changeConfirm(@RequestParam Integer id) {
        if (id != null) {
            shopcartService.deleteById(id);
        }
        return "redirect:/cart";
    }
}
