package com.wyu.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author novo
 * @since 2023-03-17
 */
@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "my_queue";

    @Bean
    public Queue queue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(QUEUE_NAME, true, false, false);
    }
}
