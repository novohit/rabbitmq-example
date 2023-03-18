package com.wyu.messagettl.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author novo
 * @since 2023-03-18
 */
@Configuration
public class RabbitConfig {

    public static final String MSG_QUEUE = "ttl_queue";

    public static final String DIRECT_EXCHANGE = "ttl_exchange";

    @Bean
    public Queue msgQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(MSG_QUEUE, true, false, false);
    }


    @Bean
    public DirectExchange directExchange() {
        // 这里配置的持久化是交换机本身能否被持久化而不是消息 消息只能在队列里持久化
        // 重启应用后交换机是否还存在
        // autoDelete:没有与之绑定的队列，是否删除交换机
        return new DirectExchange(DIRECT_EXCHANGE, false, false);
    }

    /**
     * 将队列和交换机绑定
     *
     * @return
     */
    @Bean
    Binding directBinding0() {
        return BindingBuilder.bind(msgQueue())
                .to(directExchange())
                .with(MSG_QUEUE); // 设置routing_key 这里直接用队列名字
    }
}
