package com.egou.controller;

import com.egou.domain.Commodity;
import com.egou.domain.Favorite;
import com.egou.domain.Result;
import com.egou.domain.Review;
import com.egou.domain.User;
import com.egou.service.CommodityService;
import com.egou.service.FavoriteService;
import com.egou.service.ReviewService;
import com.egou.service.ShopcartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品详情控制器
 * 对接接口：/product/{id}、/buyNow、/toggleFavorite、/addReview
 * 功能：商品详情展示、直接购买、收藏/取消收藏、商品评价
 */
@Controller
public class ProductController {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ShopcartService shopcartService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ReviewService reviewService;

    /**
     * 商品详情页
     * 请求方式：GET
     * 请求路径：/product/{id}
     * 参数：id（商品ID，路径变量）
     * 返回值：商品详情视图
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model, HttpSession session) {
        if (id == null) {
            return "redirect:/index";
        }
        Commodity commodity = commodityService.findById(id);
        if (commodity == null || commodity.getCstatus() == 0) {
            return "redirect:/index";
        }
        model.addAttribute("commodity", commodity);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        // 判断是否已收藏
        if (user != null) {
            boolean isFav = favoriteService.isFavorite(user.getId(), id);
            model.addAttribute("isFavorite", isFav);
            // 判断是否已评价
            boolean hasReviewed = reviewService.hasReviewed(user.getId(), id);
            model.addAttribute("hasReviewed", hasReviewed);
        } else {
            model.addAttribute("isFavorite", false);
            model.addAttribute("hasReviewed", false);
        }

        // 查询商品评价列表
        List<Review> reviews = reviewService.findByCommodityId(id);
        model.addAttribute("reviews", reviews);
        // 平均评分和评价数量
        model.addAttribute("avgRating", reviewService.avgRating(id));
        model.addAttribute("reviewCount", reviewService.countByCommodityId(id));

        return "product";
    }

    /**
     * 直接购买（加入购物车并跳转结算）
     * 请求方式：POST
     * 请求路径：/buyNow
     * 参数：commodityid（商品ID）、num（购买数量，默认1）
     * 返回值：统一返回结果
     */
    @PostMapping("/buyNow")
    @ResponseBody
    public Result<Object> buyNow(@RequestParam Integer commodityid,
                                  @RequestParam(defaultValue = "1") Integer num,
                                  HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(401, "请先登录", null);
        }
        if (commodityid == null) {
            return Result.fail("商品ID不能为空");
        }
        if (num == null || num <= 0) {
            num = 1;
        }
        // 检查库存
        Commodity commodity = commodityService.findById(commodityid);
        if (commodity == null || commodity.getCstatus() == 0) {
            return Result.fail("商品已下架");
        }
        if (commodity.getCnum() < num) {
            return Result.fail("库存不足");
        }
        // 加入购物车
        boolean result = shopcartService.addToCart(user.getId(), commodityid);
        if (result) {
            return Result.success("已加入购物车，请前往购物车结算");
        }
        return Result.fail("购买失败");
    }

    /**
     * 收藏/取消收藏商品
     * 请求方式：POST
     * 请求路径：/toggleFavorite
     * 参数：commodityid（商品ID）
     * 返回值：统一返回结果（data中isFavorite表示当前状态）
     */
    @PostMapping("/toggleFavorite")
    @ResponseBody
    public Result<Object> toggleFavorite(@RequestParam Integer commodityid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(401, "请先登录", null);
        }
        if (commodityid == null) {
            return Result.fail("商品ID不能为空");
        }
        boolean isFavorite = favoriteService.toggleFavorite(user.getId(), commodityid);
        Map<String, Object> data = new HashMap<>();
        data.put("isFavorite", isFavorite);
        data.put("msg", isFavorite ? "收藏成功" : "已取消收藏");
        return Result.success(isFavorite ? "收藏成功" : "已取消收藏", data);
    }

    /**
     * 提交商品评价
     * 请求方式：POST
     * 请求路径：/addReview
     * 参数：commodityid（商品ID）、content（评价内容）、rating（评分1-5）
     * 返回值：统一返回结果
     */
    @PostMapping("/addReview")
    @ResponseBody
    public Result<Object> addReview(@RequestParam Integer commodityid,
                                     @RequestParam String content,
                                     @RequestParam(defaultValue = "5") Integer rating,
                                     HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(401, "请先登录", null);
        }
        if (user.getRole() != 0) {
            return Result.fail("只有买家可以评价商品");
        }
        if (commodityid == null) {
            return Result.fail("商品ID不能为空");
        }
        if (content == null || content.trim().isEmpty()) {
            return Result.fail("评价内容不能为空");
        }
        if (rating == null || rating < 1 || rating > 5) {
            rating = 5;
        }
        // 检查是否已评价
        if (reviewService.hasReviewed(user.getId(), commodityid)) {
            return Result.fail("您已评价过该商品，不能重复评价");
        }
        Review review = new Review();
        review.setUserid(user.getId());
        review.setCommodityid(commodityid);
        review.setContent(content.trim());
        review.setRating(rating);
        boolean result = reviewService.addReview(review);
        if (result) {
            return Result.success("评价成功");
        }
        return Result.fail("评价失败");
    }

    /**
     * 检查登录状态
     * 请求方式：GET
     * 请求路径：/checkLogin
     * 返回值：统一返回结果
     */
    @GetMapping("/checkLogin")
    @ResponseBody
    public Result<Object> checkLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("name", user.getName());
            data.put("role", user.getRole());
            return Result.success("已登录", data);
        }
        return Result.build(401, "未登录", null);
    }
}
