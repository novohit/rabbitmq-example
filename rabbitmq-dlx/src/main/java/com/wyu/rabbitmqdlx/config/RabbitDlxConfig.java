package com.wyu.rabbitmqdlx.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 实际上死信交换机和死信队列就是普通的交换机和队列
 * 只是人为地去让它去处理特定的消息
 *
 * @author novo
 * @since 2023-03-18
 */
@Configuration
public class RabbitDlxConfig {

    public static final String DEAD_LETTER_QUEUE = "dead_letter_queue";

    public static final String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";

    @Bean
    public Queue deadLetterQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(DEAD_LETTER_QUEUE, true, false, false);
    }


    @Bean
    public DirectExchange deadLetterExchange() {
        // 这里配置的持久化是交换机本身能否被持久化而不是消息 消息只能在队列里持久化
        // 重启应用后交换机是否还存在
        // autoDelete:没有与之绑定的队列，是否删除交换机
        return new DirectExchange(DEAD_LETTER_EXCHANGE, false, false);
    }

    /**
     * 将队列和交换机绑定
     *
     * @return
     */
    @Bean
    Binding dlxBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_QUEUE); // 设置routing_key 这里直接用队列名字
    }
}
