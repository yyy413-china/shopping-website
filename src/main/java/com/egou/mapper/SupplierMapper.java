package com.egou.mapper;

import com.egou.domain.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商家Mapper接口
 * 商家数据的数据库交互操作
 */
@Mapper
public interface SupplierMapper {

    /**
     * 根据用户ID查询商家信息
     * @param userid 用户ID
     * @return 商家对象
     */
    Supplier findByUserId(@Param("userid") Integer userid);

    /**
     * 根据ID查询商家
     * @param id 商家ID
     * @return 商家对象
     */
    Supplier findById(@Param("id") Integer id);

    /**
     * 新增商家
     * @param supplier 商家对象
     * @return 影响行数
     */
    int insert(Supplier supplier);
}
