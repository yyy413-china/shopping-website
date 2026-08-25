package com.egou.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 留言消息实体类
 * 用于RabbitMQ消息传递
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 消息ID */
    private Integer id;
    /** 发送者用户ID */
    private Integer senderId;
    /** 发送者姓名 */
    private String senderName;
    /** 消息内容 */
    private String content;
    /** 发送时间 */
    private Date sendTime;
}
