package com.egou.service;

import com.egou.domain.Category;

import java.util.List;

/**
 * 商品分类业务接口
 */
public interface CategoryService {

    /**
     * 查询全部分类
     * @return 分类列表
     */
    List<Category> findAll();
}
