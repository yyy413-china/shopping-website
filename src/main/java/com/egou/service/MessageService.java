package com.egou.service;

import com.egou.domain.Message;

import java.util.List;

/**
 * 留言消息业务接口
 * 基于RabbitMQ实现异步消息收发
 */
public interface MessageService {

    /**
     * 发送留言消息（买家=生产者）
     * @param message 消息对象
     */
    void sendMessage(Message message);

    /**
     * 接收留言消息（商家=消费者）
     * @return 消息列表
     */
    List<Message> receiveMessages();
}
