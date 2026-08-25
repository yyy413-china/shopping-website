package com.egou.domain;

import lombok.Data;
import java.util.Date;

/**
 * 用户实体类
 * 对应数据库user表
 */
@Data
public class User {
    /** 用户ID，主键自增 */
    private Integer id;
    /** 账号 */
    private String account;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 性别 */
    private String sex;
    /** 身份证号 */
    private String idcard;
    /** 手机号 */
    private String phone;
    /** 角色：0-普通买家，1-商家 */
    private Integer role;
    /** 在线状态：0-离线，1-在线 */
    private Integer onlineyes;
    /** 最后登录时间 */
    private Date logintime;
    /** 注册时间 */
    private Date createtime;
}
