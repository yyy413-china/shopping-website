package com.egou.controller;

import com.egou.domain.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传控制器
 * 通用文件上传接口
 * 限制上传文件格式和大小
 */
@Controller
public class FileController {

    @Value("${file.upload-path}")
    private String uploadPath;

    /**
     * 通用文件上传
     * 请求方式：POST
     * 请求路径：/upload
     * 参数：file（上传文件）
     * 返回值：文件访问路径
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.fail("请选择文件");
        }
        // 校验文件格式
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!suffix.equals(".jpg") && !suffix.equals(".jpeg") && !suffix.equals(".png") && !suffix.equals(".gif")) {
            return Result.fail("仅支持上传jpg/jpeg/png/gif格式的图片");
        }
        // 校验文件大小（10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            return Result.fail("文件大小不能超过10MB");
        }
        // 生成唯一文件名
        String newFilename = UUID.randomUUID().toString() + suffix;
        try {
            File dest = new File(uploadPath + newFilename);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return Result.success("/upload/" + newFilename);
        } catch (IOException e) {
            return Result.fail("文件上传失败：" + e.getMessage());
        }
    }
}
