package com.egou.service.impl;

import com.egou.domain.Commodity;
import com.egou.domain.Order;
import com.egou.domain.Shopcart;
import com.egou.mapper.CommodityMapper;
import com.egou.mapper.OrderMapper;
import com.egou.mapper.ShopcartMapper;
import com.egou.service.ShopcartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 购物车业务实现类
 * 处理购物车增删改、订单结算等核心业务逻辑
 * 结算操作使用Spring声明式事务保证数据一致性
 */
@Service
public class ShopcartServiceImpl implements ShopcartService {

    @Autowired
    private ShopcartMapper shopcartMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询用户未结算购物车
     */
    @Override
    public List<Shopcart> findUnpaidByUserId(Integer userid) {
        if (userid == null) {
            return null;
        }
        return shopcartMapper.findUnpaidByUserId(userid);
    }

    /**
     * 查询用户已结算订单
     */
    @Override
    public List<Shopcart> findPaidByUserId(Integer userid) {
        if (userid == null) {
            return null;
        }
        return shopcartMapper.findPaidByUserId(userid);
    }

    /**
     * 加入购物车
     * 如果购物车已有该商品，则数量+1；否则新增购物车记录
     */
    @Override
    public boolean addToCart(Integer userid, Integer commodityid) {
        if (userid == null || commodityid == null) {
            return false;
        }
        // 检查购物车是否已有该商品
        Shopcart existCart = shopcartMapper.findByUserAndCommodity(userid, commodityid);
        if (existCart != null) {
            // 已有，数量+1
            return shopcartMapper.updateNum(existCart.getId(), existCart.getCnum() + 1) > 0;
        } else {
            // 新增购物车记录
            Shopcart shopcart = new Shopcart();
            shopcart.setUserid(userid);
            shopcart.setCommodityid(commodityid);
            shopcart.setCnum(1);
            shopcart.setConfirm(0);
            return shopcartMapper.insert(shopcart) > 0;
        }
    }

    /**
     * 修改购物车商品数量
     */
    @Override
    public boolean changeNum(Integer id, Integer cnum) {
        if (id == null || cnum == null || cnum <= 0) {
            return false;
        }
        return shopcartMapper.updateNum(id, cnum) > 0;
    }

    /**
     * 删除购物车商品
     */
    @Override
    public boolean deleteById(Integer id) {
        if (id == null) {
            return false;
        }
        return shopcartMapper.deleteById(id) > 0;
    }

    /**
     * 订单结算
     * 核心业务逻辑：生成唯一订单号、计算总金额、扣减库存、更新购物车状态
     * 使用Spring声明式事务保证多表操作数据一致性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String pay(Integer userid) {
        if (userid == null) {
            return null;
        }
        // 查询未结算购物车
        List<Shopcart> unpaidList = shopcartMapper.findUnpaidByUserId(userid);
        if (unpaidList == null || unpaidList.isEmpty()) {
            return null;
        }

        // 计算订单总金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Shopcart cart : unpaidList) {
            BigDecimal price = cart.getCommodityPrice();
            BigDecimal num = new BigDecimal(cart.getCnum());
            totalPrice = totalPrice.add(price.multiply(num));
        }

        // 生成唯一订单号：时间戳 + 随机数
        String orderNo = generateOrderNo();

        // 创建订单
        Order order = new Order();
        order.setOrderno(orderNo);
        order.setUserid(userid);
        order.setTotalprice(totalPrice);
        order.setOstatus(0); // 待发货
        orderMapper.insert(order);

        // 更新购物车状态并扣减库存
        for (Shopcart cart : unpaidList) {
            // 更新购物车结算状态，关联订单ID
            shopcartMapper.updateConfirm(cart.getId(), order.getId());
            // 扣减商品库存
            commodityMapper.reduceStock(cart.getCommodityid(), cart.getCnum());
        }

        return orderNo;
    }

    /**
     * 生成唯一订单号
     * 格式：yyyyMMddHHmmss + 4位随机数
     */
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000; // 1000-9999的4位随机数
        return dateStr + randomNum;
    }
}
