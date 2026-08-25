package com.egou.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 声明交换机、消息队列，完成绑定
 * 买家为消息生产者（提交留言），商家为消息消费者（接收查看留言）
 * 通过配置文件控制是否启用，未安装RabbitMQ时可设为false
 */
@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = false)
public class RabbitMQConfig {

    /** 交换机名称 */
    public static final String EGOU_EXCHANGE = "egou.exchange";
    /** 留言队列名称 */
    public static final String EGOU_QUEUE = "egou.queue";
    /** 路由键 */
    public static final String EGOU_ROUTING_KEY = "egou.routing.key";

    /**
     * 声明交换机
     * 持久化、不自动删除
     */
    @Bean
    public DirectExchange egouExchange() {
        return new DirectExchange(EGOU_EXCHANGE, true, false);
    }

    /**
     * 声明消息队列
     * 持久化、不独占、不自动删除
     */
    @Bean
    public Queue egouQueue() {
        return new Queue(EGOU_QUEUE, true, false, false);
    }

    /**
     * 将队列绑定到交换机，指定路由键
     */
    @Bean
    public Binding egouBinding(Queue egouQueue, DirectExchange egouExchange) {
        return BindingBuilder.bind(egouQueue).to(egouExchange).with(EGOU_ROUTING_KEY);
    }

    /**
     * 消息转换器
     * 使用JSON格式序列化消息，方便查看和调试
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
