package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 商品图片实体类
 * 对应数据库image表
 */
@Data
public class Image {
    /** 图片ID，主键自增 */
    private Integer id;
    /** 商品ID，外键 */
    private Integer commodityid;
    /** 图片名称 */
    private String iname;
    /** 图片路径 */
    private String ipath;
    /** 创建时间 */
    private Date createtime;
}
