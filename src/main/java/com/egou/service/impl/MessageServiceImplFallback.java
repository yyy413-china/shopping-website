package com.egou.service.impl;

import com.egou.domain.Message;
import com.egou.service.MessageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 留言消息业务实现类（无RabbitMQ降级版）
 * 当未安装RabbitMQ时自动启用，使用内存存储替代消息队列
 */
@Service
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "false", matchIfMissing = true)
public class MessageServiceImplFallback implements MessageService {

    /** 消息存储列表（内存模式） */
    private static final List<Message> messageList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void sendMessage(Message message) {
        if (message == null || message.getContent() == null) {
            return;
        }
        message.setSendTime(new Date());
        // 直接存入内存列表，不经过RabbitMQ
        messageList.add(message);
    }

    @Override
    public List<Message> receiveMessages() {
        return new ArrayList<>(messageList);
    }
}
