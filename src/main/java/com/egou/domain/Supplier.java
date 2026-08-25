package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 商家实体类
 * 对应数据库supplier表
 */
@Data
public class Supplier {
    /** 商家ID，主键自增 */
    private Integer id;
    /** 关联用户ID */
    private Integer userid;
    /** 商家名称 */
    private String sname;
    /** 商家电话 */
    private String sphone;
    /** 商家地址 */
    private String saddress;
    /** 商家状态：0-停用，1-正常 */
    private Integer sstatus;
    /** 创建时间 */
    private Date createtime;
}
