package com.egou.mapper;

import com.egou.domain.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品分类Mapper接口
 * 商品分类数据的数据库交互操作
 */
@Mapper
public interface CategoryMapper {

    /**
     * 查询全部分类
     * @return 分类列表
     */
    List<Category> findAll();
}
