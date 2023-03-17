package com.wyu.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DirectExchange 的路由策略是将消息队列绑定到一个 DirectExchange 上
 * 当一条消息到达 DirectExchange 时会被转发到与该条消息 routing key 相同的 Queue 上
 *
 * @author novo
 * @since 2023-03-17
 */
@Configuration
public class RabbitDirectConfig {

    public static final String DIRECT_QUEUE_0 = "direct_queue_0";

    public static final String DIRECT_QUEUE_1 = "direct_queue_1";

    public static final String DIRECT_EXCHANGE_NAME = "direct_exchange";

    @Bean
    public Queue queue0() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(DIRECT_QUEUE_0, true, false, false);
    }

    @Bean
    public Queue queue1() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(DIRECT_QUEUE_1, true, false, false);
    }

    /**
     * 直连交换机
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        // 这里配置的持久化是交换机本身能否被持久化而不是消息 消息只能在队列里持久化
        // 重启应用后交换机是否还存在
        // autoDelete:没有与之绑定的队列，是否删除交换机
        return new DirectExchange(DIRECT_EXCHANGE_NAME, false, false);
    }

    /**
     * 将队列和交换机绑定
     *
     * @return
     */
    @Bean
    Binding directBinding0() {
        return BindingBuilder.bind(queue0())
                .to(directExchange())
                .with(DIRECT_QUEUE_0); // 设置routing_key 这里直接用队列名字
    }

    @Bean
    Binding directBinding1() {
        return BindingBuilder.bind(queue1())
                .to(directExchange())
                .with(DIRECT_QUEUE_1); // 设置routing_key 这里直接用队列名字
    }
}
