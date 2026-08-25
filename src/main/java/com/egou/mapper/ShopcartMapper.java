package com.egou.mapper;

import com.egou.domain.Shopcart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车Mapper接口
 * 购物车数据的数据库交互操作
 */
@Mapper
public interface ShopcartMapper {

    /**
     * 根据用户ID查询未结算购物车（联表查询商品信息）
     * @param userid 用户ID
     * @return 购物车列表
     */
    List<Shopcart> findUnpaidByUserId(@Param("userid") Integer userid);

    /**
     * 根据用户ID查询已结算订单（联表查询商品和订单信息）
     * @param userid 用户ID
     * @return 购物车列表
     */
    List<Shopcart> findPaidByUserId(@Param("userid") Integer userid);

    /**
     * 查询用户购物车中是否已有该商品
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 购物车对象
     */
    Shopcart findByUserAndCommodity(@Param("userid") Integer userid, @Param("commodityid") Integer commodityid);

    /**
     * 新增购物车记录
     * @param shopcart 购物车对象
     * @return 影响行数
     */
    int insert(Shopcart shopcart);

    /**
     * 修改购物车商品数量
     * @param id 购物车ID
     * @param cnum 新数量
     * @return 影响行数
     */
    int updateNum(@Param("id") Integer id, @Param("cnum") Integer cnum);

    /**
     * 删除购物车记录
     * @param id 购物车ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新购物车结算状态（结算后关联订单ID）
     * @param id 购物车ID
     * @param orderid 订单ID
     * @return 影响行数
     */
    int updateConfirm(@Param("id") Integer id, @Param("orderid") Integer orderid);

    /**
     * 统计商家已结算订单的总销售额
     * @param supplierid 商家ID
     * @return 总销售额
     */
    java.math.BigDecimal sumSalesBySupplierId(@Param("supplierid") Integer supplierid);

    /**
     * 统计商家已结算订单的总销量（商品件数）
     * @param supplierid 商家ID
     * @return 总销量
     */
    int sumSalesNumBySupplierId(@Param("supplierid") Integer supplierid);

    /**
     * 统计商家已结算订单数
     * @param supplierid 商家ID
     * @return 订单数
     */
    int countOrdersBySupplierId(@Param("supplierid") Integer supplierid);

    /**
     * 查询商家最近售出的商品列表（联表查询）
     * @param supplierid 商家ID
     * @return 购物车列表
     */
    List<Shopcart> findRecentSalesBySupplierId(@Param("supplierid") Integer supplierid);

    /**
     * 查询商家所有已售出的商品列表（联表查询，用于明细页）
     * @param supplierid 商家ID
     * @return 购物车列表
     */
    List<Shopcart> findAllSalesBySupplierId(@Param("supplierid") Integer supplierid);
}
