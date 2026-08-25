package com.egou.controller;

import com.egou.domain.Message;
import com.egou.domain.Result;
import com.egou.domain.Review;
import com.egou.domain.Supplier;
import com.egou.domain.User;
import com.egou.service.MessageService;
import com.egou.service.ReviewService;
import com.egou.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * 留言控制器
 * 对接接口：/board、/sendMsg、/receiveMsg
 * 功能：买家发送留言（RabbitMQ生产者）、商家接收留言和商品评价
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SupplierService supplierService;

    /**
     * 留言板页面
     * 请求方式：GET
     * 请求路径：/board
     * 功能：根据角色展示不同区域（买家-发送留言，商家-查看留言+商品评价）
     */
    @GetMapping("/board")
    public String boardPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        // 商家可以查看所有留言和商品评价
        if (user.getRole() == 1) {
            List<Message> messages = messageService.receiveMessages();
            model.addAttribute("messages", messages);

            // 查询商家所有商品的评价
            Supplier supplier = supplierService.findByUserId(user.getId());
            if (supplier != null) {
                List<Review> reviews = reviewService.findBySupplierId(supplier.getId());
                model.addAttribute("reviews", reviews);
            }
        }
        return "board";
    }

    /**
     * 发送留言消息（买家=生产者）
     * 请求方式：POST
     * 请求路径：/sendMsg
     * 参数：content（留言内容）
     */
    @PostMapping("/sendMsg")
    @ResponseBody
    public Result<Object> sendMsg(@RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.build(401, "请先登录", null);
        }
        // 只有买家可以发送留言
        if (user.getRole() != 0) {
            return Result.fail("只有买家可以发送留言");
        }
        if (content == null || content.trim().isEmpty()) {
            return Result.fail("留言内容不能为空");
        }
        Message message = new Message();
        message.setSenderId(user.getId());
        message.setSenderName(user.getName());
        message.setContent(content.trim());
        messageService.sendMessage(message);
        return Result.success("留言发送成功");
    }

    /**
     * 接收留言消息（商家=消费者）
     * 请求方式：GET
     * 请求路径：/receiveMsg
     */
    @GetMapping("/receiveMsg")
    @ResponseBody
    public Result<List<Message>> receiveMsg() {
        List<Message> messages = messageService.receiveMessages();
        return Result.success(messages);
    }
}
