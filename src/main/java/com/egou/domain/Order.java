package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 订单实体类
 * 对应数据库order表
 */
@Data
public class Order {
    /** 订单ID，主键自增 */
    private Integer id;
    /** 订单编号（唯一） */
    private String orderno;
    /** 用户ID，外键 */
    private Integer userid;
    /** 订单总金额 */
    private java.math.BigDecimal totalprice;
    /** 订单状态：0-待发货，1-已发货，2-已完成 */
    private Integer ostatus;
    /** 创建时间 */
    private Date createtime;
    /** 用户姓名（联表查询用） */
    private String userName;
}
