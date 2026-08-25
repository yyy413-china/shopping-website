package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 商品实体类
 * 对应数据库commodity表
 */
@Data
public class Commodity {
    /** 商品ID，主键自增 */
    private Integer id;
    /** 商家ID，外键 */
    private Integer supplierid;
    /** 分类ID，外键 */
    private Integer categoryid;
    /** 商品名称 */
    private String cname;
    /** 商品价格 */
    private java.math.BigDecimal cprice;
    /** 库存数量 */
    private Integer cnum;
    /** 商品描述 */
    private String cdesc;
    /** 商品状态：0-停售，1-在售 */
    private Integer cstatus;
    /** 创建时间 */
    private Date createtime;
    /** 商品图片路径（联表查询用，非数据库字段） */
    private String ipath;
    /** 分类名称（联表查询用，非数据库字段） */
    private String categoryName;
    /** 商家名称（联表查询用，非数据库字段） */
    private String supplierName;
}
