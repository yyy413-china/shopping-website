package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 收藏实体类
 * 对应数据库favorite表
 */
@Data
public class Favorite {
    /** 收藏ID，主键自增 */
    private Integer id;
    /** 用户ID，外键 */
    private Integer userid;
    /** 商品ID，外键 */
    private Integer commodityid;
    /** 收藏时间 */
    private Date createtime;
    /** 商品名称（联表查询用） */
    private String cname;
    /** 商品价格（联表查询用） */
    private java.math.BigDecimal cprice;
    /** 商品图片路径（联表查询用） */
    private String ipath;
    /** 商品状态（联表查询用） */
    private Integer cstatus;
}
