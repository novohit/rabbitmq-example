package com.wyu.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author novo
 * @since 2023-03-18
 */
@Configuration
public class RabbitTopicConfig {

    public static final String XIAOMI_QUEUE = "xiaomi_queue";

    public static final String HUAWEI_QUEUE = "huawei_queue";

    public static final String PHONE_QUEUE = "phone_queue";

    public static final String TOPIC_EXCHANGE = "topic_exchange";

    @Bean
    public Queue xiaomiQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(XIAOMI_QUEUE, true, false, false);
    }

    @Bean
    public Queue huaweiQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(HUAWEI_QUEUE, true, false, false);
    }

    @Bean
    public Queue phoneQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(PHONE_QUEUE, true, false, false);
    }

    /**
     * topic交换机
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        // 这里配置的持久化是交换机本身能否被持久化而不是消息 消息只能在队列里持久化
        // 重启应用后交换机是否还存在
        // autoDelete:没有与之绑定的队列，是否删除交换机
        return new TopicExchange(TOPIC_EXCHANGE, false, false);
    }

    /**
     * 将队列和交换机绑定
     *
     * @return
     */
    @Bean
    Binding topicBinding0() {
        return BindingBuilder.bind(xiaomiQueue())
                .to(topicExchange())
                .with("xiaomi.#"); //这里的 # 是一个通配符，表示将来消息的 routing_key 只要是以 xiaomi. 开头，都将被路由到 xiaomiQueue
    }

    @Bean
    Binding topicBinding1() {
        return BindingBuilder.bind(huaweiQueue())
                .to(topicExchange())
                .with("huawei.#");
    }

    @Bean
    Binding topicBinding2() {
        return BindingBuilder.bind(phoneQueue())
                .to(topicExchange())
                .with("#.phone.#"); // routing_key只要包含.phone. 就会被路由到phoneQueue 如 xiaomi.phone.news 将被路由到phoneQueue和xiaomiQueue
    }
}
