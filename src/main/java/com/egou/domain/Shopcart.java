package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 购物车实体类
 * 对应数据库shopcart表
 */
@Data
public class Shopcart {
    /** 购物车ID，主键自增 */
    private Integer id;
    /** 用户ID，外键 */
    private Integer userid;
    /** 商品ID，外键 */
    private Integer commodityid;
    /** 购买数量 */
    private Integer cnum;
    /** 结算状态：0-未结算，1-已结算 */
    private Integer confirm;
    /** 关联订单ID */
    private Integer orderid;
    /** 创建时间 */
    private Date createtime;
    /** 商品名称（联表查询用） */
    private String commodityName;
    /** 商品价格（联表查询用） */
    private java.math.BigDecimal commodityPrice;
    /** 商品图片路径（联表查询用） */
    private String imagePath;
    /** 商品库存（联表查询用） */
    private Integer commodityNum;
    /** 订单编号（联表查询用） */
    private String orderNo;
}
