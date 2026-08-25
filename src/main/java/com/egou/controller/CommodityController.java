package com.egou.controller;

import com.egou.domain.Commodity;
import com.egou.domain.Image;
import com.egou.domain.Result;
import com.egou.domain.Supplier;
import com.egou.domain.User;
import com.egou.mapper.ImageMapper;
import com.egou.service.CommodityService;
import com.egou.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Controller
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ImageMapper imageMapper;

    /** 文件上传路径 */
    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 商家商品管理页面
     * 请求方式：GET
     * 请求路径：/com
     * 功能：展示商家商品列表
     */
    @GetMapping("/com")
    public String commodityPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/403";
        }
        // 查询商家信息
        Supplier supplier = supplierService.findByUserId(user.getId());
        if (supplier == null) {
            return "redirect:/403";
        }
        // 查询该商家的商品列表
        List<Commodity> commodities = commodityService.findBySupplierId(supplier.getId());
        model.addAttribute("commodities", commodities);
        model.addAttribute("supplier", supplier);
        model.addAttribute("user", user);
        return "com";
    }

    /**
     * 新增商品
     * 请求方式：POST
     * 请求路径：/add
     * 参数：商品信息 + 图片文件
     * 功能：新增商品同时上传图片，双表联动
     */
    @PostMapping("/add")
    @ResponseBody
    public Result<Object> add(Commodity commodity, @RequestParam("file") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.build(403, "无权限操作", null);
        }
        // 参数校验
        if (commodity.getCname() == null || commodity.getCname().trim().isEmpty()) {
            return Result.fail("商品名称不能为空");
        }
        if (commodity.getCprice() == null) {
            return Result.fail("商品价格不能为空");
        }

        // 获取商家信息
        Supplier supplier = supplierService.findByUserId(user.getId());
        commodity.setSupplierid(supplier.getId());

        // 处理文件上传
        String imagePath = null;
        String imageName = null;
        if (file != null && !file.isEmpty()) {
            // 校验文件格式
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!suffix.equals(".jpg") && !suffix.equals(".jpeg") && !suffix.equals(".png") && !suffix.equals(".gif")) {
                return Result.fail("仅支持上传jpg/jpeg/png/gif格式的图片");
            }
            // 校验文件大小（10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return Result.fail("图片大小不能超过10MB");
            }
            // 生成唯一文件名
            String newFilename = UUID.randomUUID().toString() + suffix;
            imagePath = "/upload/" + newFilename;
            imageName = originalFilename;
            // 保存文件到服务器
            try {
                File dest = new File(uploadPath + newFilename);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
            } catch (IOException e) {
                return Result.fail("图片上传失败：" + e.getMessage());
            }
        }

        // 新增商品（同时新增图片，双表联动）
        boolean result = commodityService.addCommodity(commodity, imagePath, imageName);
        if (result) {
            return Result.success("新增商品成功");
        }
        return Result.fail("新增商品失败");
    }

    /**
     * 修改商品
     * 请求方式：POST
     * 请求路径：/up
     * 参数：商品信息
     */
    @PostMapping("/up")
    @ResponseBody
    public Result<Object> update(Commodity commodity, @RequestParam(value = "file", required = false) MultipartFile file) {
        // 参数校验
        if (commodity.getId() == null) {
            return Result.fail("商品ID不能为空");
        }
        if (commodity.getCname() == null || commodity.getCname().trim().isEmpty()) {
            return Result.fail("商品名称不能为空");
        }

        // 处理文件上传（如果上传了新图片）
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!suffix.equals(".jpg") && !suffix.equals(".jpeg") && !suffix.equals(".png") && !suffix.equals(".gif")) {
                return Result.fail("仅支持上传jpg/jpeg/png/gif格式的图片");
            }
            String newFilename = UUID.randomUUID().toString() + suffix;
            String imagePath = "/upload/" + newFilename;
            try {
                File dest = new File(uploadPath + newFilename);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
                // 更新图片
                Image image = new Image();
                image.setCommodityid(commodity.getId());
                image.setIname(originalFilename);
                image.setIpath(imagePath);
                imageMapper.updateByCommodityId(image);
            } catch (IOException e) {
                return Result.fail("图片上传失败：" + e.getMessage());
            }
        }

        boolean result = commodityService.updateCommodity(commodity);
        if (result) {
            return Result.success("修改商品成功");
        }
        return Result.fail("修改商品失败");
    }

    /**
     * 删除商品
     * 请求方式：GET
     * 请求路径：/show（删除操作）
     * 参数：id（商品ID）、op（操作类型：delete-删除，stop-停售，start-在售）
     */
    @GetMapping("/show")
    public String show(@RequestParam Integer id, @RequestParam String op) {
        if (id == null) {
            return "redirect:/com";
        }
        if ("delete".equals(op)) {
            commodityService.deleteCommodity(id);
        } else if ("stop".equals(op)) {
            commodityService.updateStatus(id, 0);
        } else if ("start".equals(op)) {
            commodityService.updateStatus(id, 1);
        }
        return "redirect:/com";
    }

    /**
     * 获取商品图片
     * 请求方式：GET
     * 请求路径：/getpic
     * 参数：commodityid（商品ID）
     * 返回值：图片路径
     */
    @GetMapping("/getpic")
    @ResponseBody
    public Result<Image> getpic(@RequestParam Integer commodityid) {
        if (commodityid == null) {
            return Result.fail("商品ID不能为空");
        }
        Image image = imageMapper.findByCommodityId(commodityid);
        if (image != null) {
            return Result.success(image);
        }
        return Result.fail("图片不存在");
    }
}
