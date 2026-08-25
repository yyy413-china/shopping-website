package com.egou.controller;

import com.egou.domain.Commodity;
import com.egou.domain.Favorite;
import com.egou.domain.Order;
import com.egou.domain.Result;
import com.egou.domain.Supplier;
import com.egou.domain.User;
import com.egou.mapper.OrderMapper;
import com.egou.mapper.ShopcartMapper;
import com.egou.service.CommodityService;
import com.egou.service.FavoriteService;
import com.egou.service.SupplierService;
import com.egou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人主页控制器
 * 对接接口：/profile、/updateProfile、/getOrderStats
 * 功能：个人主页展示（买家/商家差异化）、个人信息修改、订单统计
 */
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ShopcartMapper shopcartMapper;

    /**
     * 个人主页（买家/商家差异化展示）
     * 请求方式：GET
     * 请求路径：/profile
     * 返回值：个人主页视图
     */
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // 查询最新用户信息
        User latestUser = userService.findById(user.getId());
        if (latestUser != null) {
            model.addAttribute("user", latestUser);
            session.setAttribute("user", latestUser);
        } else {
            model.addAttribute("user", user);
        }

        // 商家：展示商家专属数据
        if (user.getRole() == 1) {
            Supplier supplier = supplierService.findByUserId(user.getId());
            model.addAttribute("supplier", supplier);

            if (supplier != null) {
                int productCount = commodityService.findBySupplierId(supplier.getId()).size();
                int orderCount = shopcartMapper.countOrdersBySupplierId(supplier.getId());
                int totalSales = shopcartMapper.sumSalesNumBySupplierId(supplier.getId());
                List<Commodity> commodities = commodityService.findBySupplierId(supplier.getId());

                model.addAttribute("productCount", productCount);
                model.addAttribute("orderCount", orderCount);
                model.addAttribute("totalSales", totalSales);
                model.addAttribute("commodities", commodities);
            }
            return "seller_profile";
        }

        // 买家：展示买家专属数据
        int orderCount = orderMapper.countByUserId(user.getId());
        model.addAttribute("orderCount", orderCount);

        int favoriteCount = favoriteService.countByUserId(user.getId());
        model.addAttribute("favoriteCount", favoriteCount);

        List<Order> orders = orderMapper.findByUserId(user.getId());
        model.addAttribute("orders", orders);

        List<Favorite> favorites = favoriteService.findByUserId(user.getId());
        model.addAttribute("favorites", favorites);

        return "profile";
    }

    /**
     * 更新个人信息
     * 请求方式：POST
     * 请求路径：/updateProfile
     * 参数：name、sex、phone
     * 返回值：统一返回结果
     */
    @PostMapping("/updateProfile")
    @ResponseBody
    public Result<Object> updateProfile(@RequestParam String name,
                                         @RequestParam String sex,
                                         @RequestParam String phone,
                                         HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(401, "请先登录", null);
        }
        if (name == null || name.trim().isEmpty()) {
            return Result.fail("姓名不能为空");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setName(name.trim());
        updateUser.setSex(sex);
        updateUser.setPhone(phone);
        return Result.success("修改成功");
    }

    /**
     * 获取订单统计数据
     * 请求方式：GET
     * 请求路径：/getOrderStats
     * 返回值：统一返回结果
     */
    @GetMapping("/getOrderStats")
    @ResponseBody
    public Result<Object> getOrderStats(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(401, "请先登录", null);
        }
        Map<String, Object> stats = new HashMap<>();
        stats.put("orderCount", orderMapper.countByUserId(user.getId()));
        stats.put("favoriteCount", favoriteService.countByUserId(user.getId()));
        return Result.success(stats);
    }
}
