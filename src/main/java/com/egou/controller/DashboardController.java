package com.egou.controller;

import com.egou.domain.Commodity;
import com.egou.domain.Result;
import com.egou.domain.Shopcart;
import com.egou.domain.Supplier;
import com.egou.domain.User;
import com.egou.mapper.ShopcartMapper;
import com.egou.service.CommodityService;
import com.egou.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家仪表盘控制器
 * 对接接口：/dashboard、/dashboard/stats
 * 功能：商家首页展示营业额、销量、订单数等统计数据
 * 权限：仅商家可访问
 */
@Controller
public class DashboardController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ShopcartMapper shopcartMapper;

    /**
     * 商家仪表盘页面
     * 请求方式：GET
     * 请求路径：/dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/403";
        }
        Supplier supplier = supplierService.findByUserId(user.getId());
        if (supplier == null) {
            return "redirect:/403";
        }

        // 统计数据
        BigDecimal totalSales = shopcartMapper.sumSalesBySupplierId(supplier.getId());
        int totalSalesNum = shopcartMapper.sumSalesNumBySupplierId(supplier.getId());
        int orderCount = shopcartMapper.countOrdersBySupplierId(supplier.getId());
        int productCount = commodityService.findBySupplierId(supplier.getId()).size();

        // 最近售出商品
        List<Shopcart> recentSales = shopcartMapper.findRecentSalesBySupplierId(supplier.getId());

        // 商家商品列表
        List<Commodity> commodities = commodityService.findBySupplierId(supplier.getId());

        model.addAttribute("user", user);
        model.addAttribute("supplier", supplier);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalSalesNum", totalSalesNum);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("productCount", productCount);
        model.addAttribute("recentSales", recentSales);
        model.addAttribute("commodities", commodities);

        return "dashboard";
    }

    /**
     * 获取统计数据（AJAX）
     * 请求方式：GET
     * 请求路径：/dashboard/stats
     */
    @GetMapping("/dashboard/stats")
    @ResponseBody
    public Result<Object> getStats(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.build(401, "请先登录", null);
        }
        Supplier supplier = supplierService.findByUserId(user.getId());
        if (supplier == null) {
            return Result.fail("商家信息不存在");
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSales", shopcartMapper.sumSalesBySupplierId(supplier.getId()));
        stats.put("totalSalesNum", shopcartMapper.sumSalesNumBySupplierId(supplier.getId()));
        stats.put("orderCount", shopcartMapper.countOrdersBySupplierId(supplier.getId()));
        stats.put("productCount", commodityService.findBySupplierId(supplier.getId()).size());

        return Result.success(stats);
    }

    /**
     * 订单明细页面
     * 请求方式：GET
     * 请求路径：/dashboard/orders
     */
    @GetMapping("/dashboard/orders")
    public String orderDetails(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/403";
        }
        Supplier supplier = supplierService.findByUserId(user.getId());
        if (supplier == null) {
            return "redirect:/403";
        }

        // 查询所有已售出的购物车记录（即订单明细）
        List<Shopcart> allSales = shopcartMapper.findAllSalesBySupplierId(supplier.getId());

        // 计算合计
        int totalQty = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Shopcart sale : allSales) {
            totalQty += sale.getCnum();
            totalAmount = totalAmount.add(sale.getCommodityPrice().multiply(BigDecimal.valueOf(sale.getCnum())));
        }

        model.addAttribute("user", user);
        model.addAttribute("supplier", supplier);
        model.addAttribute("allSales", allSales);
        model.addAttribute("totalQty", totalQty);
        model.addAttribute("totalAmount", totalAmount);

        return "dashboard_orders";
    }

    /**
     * 销量明细页面
     * 请求方式：GET
     * 请求路径：/dashboard/sales
     */
    @GetMapping("/dashboard/sales")
    public String salesDetails(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/403";
        }
        Supplier supplier = supplierService.findByUserId(user.getId());
        if (supplier == null) {
            return "redirect:/403";
        }

        // 查询所有已售出的购物车记录
        List<Shopcart> allSales = shopcartMapper.findAllSalesBySupplierId(supplier.getId());

        // 计算合计
        int totalQty = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Shopcart sale : allSales) {
            totalQty += sale.getCnum();
            totalAmount = totalAmount.add(sale.getCommodityPrice().multiply(BigDecimal.valueOf(sale.getCnum())));
        }

        model.addAttribute("user", user);
        model.addAttribute("supplier", supplier);
        model.addAttribute("allSales", allSales);
        model.addAttribute("totalQty", totalQty);
        model.addAttribute("totalAmount", totalAmount);

        return "dashboard_sales";
    }

    /**
     * 商品明细页面
     * 请求方式：GET
     * 请求路径：/dashboard/products
     */
    @GetMapping("/dashboard/products")
    public String productDetails(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/403";
        }
        Supplier supplier = supplierService.findByUserId(user.getId());
        if (supplier == null) {
            return "redirect:/403";
        }

        List<Commodity> commodities = commodityService.findBySupplierId(supplier.getId());

        // 计算合计
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalStock = 0;
        for (Commodity com : commodities) {
            totalPrice = totalPrice.add(com.getCprice());
            totalStock += com.getCnum();
        }

        model.addAttribute("user", user);
        model.addAttribute("supplier", supplier);
        model.addAttribute("commodities", commodities);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalStock", totalStock);

        return "dashboard_products";
    }
}
