package com.egou.mapper;

import com.egou.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper接口
 * 订单数据的数据库交互操作
 */
@Mapper
public interface OrderMapper {

    /**
     * 新增订单
     * @param order 订单对象
     * @return 影响行数
     */
    int insert(Order order);

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单对象
     */
    Order findById(@Param("id") Integer id);

    /**
     * 根据订单编号查询订单
     * @param orderno 订单编号
     * @return 订单对象
     */
    Order findByOrderNo(@Param("orderno") String orderno);

    /**
     * 根据用户ID查询全部订单
     * @param userid 用户ID
     * @return 订单列表
     */
    List<Order> findByUserId(@Param("userid") Integer userid);

    /**
     * 统计用户订单数量
     * @param userid 用户ID
     * @return 订单数量
     */
    int countByUserId(@Param("userid") Integer userid);
}
