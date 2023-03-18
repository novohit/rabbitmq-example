package com.wyu.delaybydlx.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置死信队列
 *
 * @author novo
 * @since 2023-03-18
 */
@Configuration
public class RabbitConfig {

    public static final String DELAY_QUEUE = "delay_dxl_queue";

    public static final String DIRECT_EXCHANGE = "delay_dxl_exchange";

    public static final String DEAD_LETTER_QUEUE = "dlx_queue";

    public static final String DEAD_LETTER_EXCHANGE = "dlx_exchange";

    @Bean
    public Queue delayQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 5 * 1000); // 给队列设置过期时间
        args.put("x-dead-letter-exchange", RabbitConfig.DEAD_LETTER_EXCHANGE); // 设置死信交换机
        args.put("x-dead-letter-routing-key", RabbitConfig.DEAD_LETTER_QUEUE); // 设置死信队列的routing-key
        return new Queue(DELAY_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue deadQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(DEAD_LETTER_QUEUE, true, false, false);
    }


    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, false, false);
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, false, false);
    }

    /**
     * 将队列和交换机绑定
     *
     * @return
     */
    @Bean
    Binding delayBinding() {
        return BindingBuilder.bind(delayQueue())
                .to(delayExchange())
                .with(DELAY_QUEUE); // 设置routing_key 这里直接用队列名字
    }

    @Bean
    Binding deadBinding() {
        return BindingBuilder.bind(deadQueue())
                .to(deadExchange())
                .with(DEAD_LETTER_QUEUE); // 设置routing_key 这里直接用队列名字
    }
}
