package com.egou.controller;

import com.egou.domain.Category;
import com.egou.domain.Commodity;
import com.egou.domain.User;
import com.egou.service.CategoryService;
import com.egou.service.CommodityService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * 首页控制器
 * 对接接口：/、/index、/getpic
 * 功能：查询全部分类、按分类筛选商品、PageHelper分页查询、商品图片回显
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommodityService commodityService;

    /**
     * 首页
     * 请求方式：GET
     * 请求路径：/ 或 /index
     * 参数：categoryid（可选，分类ID）、keyword（可选，搜索关键词）、pageNum（可选，页码，默认1）、pageSize（可选，每页数量，默认8）
     * 返回值：首页视图，携带分类列表、商品分页数据
     * 说明：商家自动跳转到仪表盘，买家/游客显示商品首页
     */
    @GetMapping({"/", "/index"})
    public String index(@RequestParam(required = false) Integer categoryid,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "8") int pageSize,
                        Model model, HttpSession session) {
        // 获取登录用户信息
        User user = (User) session.getAttribute("user");

        // 商家自动跳转到仪表盘
        if (user != null && user.getRole() == 1) {
            return "redirect:/dashboard";
        }

        // 始终将keyword放入model，确保模板能正确判断搜索模式
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        model.addAttribute("keyword", searchKeyword);

        // 搜索功能需要登录
        boolean needLogin = (searchKeyword != null && user == null);
        model.addAttribute("needLogin", needLogin);

        // 查询全部分类
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        // 根据搜索关键词、分类ID筛选商品或查询全部
        PageInfo<Commodity> pageInfo;
        if (searchKeyword != null && user != null) {
            // 搜索模式（需登录）
            pageInfo = commodityService.searchByKeyword(searchKeyword, pageNum, pageSize);
        } else if (categoryid != null && categoryid > 0) {
            // 分类筛选模式
            pageInfo = commodityService.findByCategoryId(categoryid, pageNum, pageSize);
            model.addAttribute("categoryid", categoryid);
        } else {
            // 全部商品模式
            pageInfo = commodityService.findAll(pageNum, pageSize);
        }
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("user", user);

        return "index";
    }

    /**
     * 403权限拦截页面
     * 请求方式：GET
     * 请求路径：/403
     */
    @GetMapping("/403")
    public String forbidden() {
        return "403";
    }
}
