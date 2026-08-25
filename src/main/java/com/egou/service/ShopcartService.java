package com.egou.service;

import com.egou.domain.Shopcart;

import java.util.List;

/**
 * 购物车业务接口
 */
public interface ShopcartService {

    /**
     * 查询用户未结算购物车
     * @param userid 用户ID
     * @return 购物车列表
     */
    List<Shopcart> findUnpaidByUserId(Integer userid);

    /**
     * 查询用户已结算订单
     * @param userid 用户ID
     * @return 购物车列表
     */
    List<Shopcart> findPaidByUserId(Integer userid);

    /**
     * 加入购物车
     * @param userid 用户ID
     * @param commodityid 商品ID
     * @return 是否成功
     */
    boolean addToCart(Integer userid, Integer commodityid);

    /**
     * 修改购物车商品数量
     * @param id 购物车ID
     * @param cnum 新数量
     * @return 是否成功
     */
    boolean changeNum(Integer id, Integer cnum);

    /**
     * 删除购物车商品
     * @param id 购物车ID
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 订单结算（生成订单、扣减库存、更新购物车状态）
     * @param userid 用户ID
     * @return 订单编号
     */
    String pay(Integer userid);
}
