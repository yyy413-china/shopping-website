package com.egou.core;

import com.egou.domain.Result;

/**
 * 返回结果工具类
 * 快速构建统一返回结果
 */
public class ResultUtil {

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 成功返回（仅消息）
     */
    public static <T> Result<T> success(String msg) {
        return Result.success(msg);
    }

    /**
     * 成功返回（带消息和数据）
     */
    public static <T> Result<T> success(String msg, T data) {
        return Result.success(msg, data);
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> fail(String msg) {
        return Result.fail(msg);
    }

    /**
     * 参数错误返回
     */
    public static <T> Result<T> paramError(String msg) {
        return Result.build(400, msg, null);
    }

    /**
     * 无权限返回
     */
    public static <T> Result<T> forbidden(String msg) {
        return Result.build(403, msg, null);
    }
}
