package com.egou.service.impl;

import com.egou.config.RabbitMQConfig;
import com.egou.domain.Message;
import com.egou.service.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 留言消息业务实现类
 * 买家=消息生产者（提交留言），商家=消息消费者（接收查看留言）
 * 使用RabbitMQ实现异步消息收发
 * 未安装RabbitMQ时自动降级为内存存储模式
 */
@Service
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = false)
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /** 消息存储列表（模拟持久化，实际项目应存入数据库） */
    private static final List<Message> messageList = Collections.synchronizedList(new ArrayList<>());

    /**
     * 发送留言消息（买家=生产者）
     * 将消息发送到RabbitMQ交换机
     */
    @Override
    public void sendMessage(Message message) {
        if (message == null || message.getContent() == null) {
            return;
        }
        message.setSendTime(new Date());
        // 发送消息到RabbitMQ交换机
        rabbitTemplate.convertAndSend(RabbitMQConfig.EGOU_EXCHANGE, RabbitMQConfig.EGOU_ROUTING_KEY, message);
    }

    /**
     * 接收留言消息（商家=消费者）
     * 返回所有已接收的消息列表
     */
    @Override
    public List<Message> receiveMessages() {
        return new ArrayList<>(messageList);
    }

    /**
     * RabbitMQ队列监听器
     * 自动接收队列中的消息并存储
     */
    @RabbitListener(queues = RabbitMQConfig.EGOU_QUEUE)
    public void onMessage(Message message) {
        if (message != null) {
            messageList.add(message);
        }
    }
}
