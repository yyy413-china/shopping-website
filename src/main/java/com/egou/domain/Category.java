package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 商品分类实体类
 * 对应数据库category表
 */
@Data
public class Category {
    /** 分类ID，主键自增 */
    private Integer id;
    /** 分类名称 */
    private String cname;
    /** 创建时间 */
    private Date createtime;
}
