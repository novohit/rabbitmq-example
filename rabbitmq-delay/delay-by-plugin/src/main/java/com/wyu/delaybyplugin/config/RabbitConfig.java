package com.wyu.delaybyplugin.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author novo
 * @since 2023-03-18
 */
@Configuration
public class RabbitConfig {

    public static final String DELAY_QUEUE = "delay_queue";

    public static final String DIRECT_EXCHANGE = "delay_exchange";

    @Bean
    public Queue delayQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(DELAY_QUEUE, true, false, false);
    }


    /**
     * 注意延迟消息队列的交换机要用CustomExchange去设置
     *
     * @return
     */
    @Bean
    public CustomExchange delayExchange() {

        // 固定参数x-delayed-message
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct"); // 设置交换机类型
        return new CustomExchange(DIRECT_EXCHANGE, "x-delayed-message", false, false, args);
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
                .with(DELAY_QUEUE) // 设置routing_key 这里直接用队列名字
                .noargs(); // 多调用一个noargs
    }
}
