package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 商品评价实体类
 * 对应数据库review表
 */
@Data
public class Review {
    /** 评价ID，主键自增 */
    private Integer id;
    /** 用户ID，外键 */
    private Integer userid;
    /** 商品ID，外键 */
    private Integer commodityid;
    /** 评价内容 */
    private String content;
    /** 评分（1-5星） */
    private Integer rating;
    /** 评价时间 */
    private Date createtime;
    /** 用户姓名（联表查询用） */
    private String userName;
    /** 商品名称（联表查询用） */
    private String commodityName;
    /** 商品图片路径（联表查询用） */
    private String ipath;
}
