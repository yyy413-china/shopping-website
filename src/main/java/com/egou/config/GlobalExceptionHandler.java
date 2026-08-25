package com.egou.config;

import com.egou.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 统一捕获和处理系统异常，返回友好的错误信息
 * 避免将异常堆栈信息直接暴露给用户
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理静态资源未找到异常
     * 不拦截，让Spring返回404
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public void handleNoResourceFoundException(NoResourceFoundException e) {
        // 不处理，让Spring Boot返回默认404
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result<Object> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        logger.error("空指针异常，请求路径：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.fail("系统异常：数据不存在，请刷新后重试");
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result<Object> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        logger.error("非法参数异常，请求路径：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.fail("参数错误：" + e.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result<Object> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        logger.error("运行时异常，请求路径：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.fail("操作失败：" + e.getMessage());
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        logger.error("系统异常，请求路径：{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.fail("系统繁忙，请稍后重试");
    }
}
