package com.wyu.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HeadersExchange 是一种使用较少的路由策略，HeadersExchange 会根据消息的 Header 将消息路由到不同的 Queue 上，这种策略也和 routingkey无关
 *
 * @author novo
 * @since 2023-03-18
 */
@Configuration
public class RabbitHeaderConfig {
    public static final String HEADER_QUEUE_NAME = "header_queue_name";
    public static final String HEADER_QUEUE_AGE = "header_queue_age";
    public static final String HEADER_EXCHANGE_NAME = "header_exchange";

    @Bean
    Queue headerNameQueue() {
        return new Queue(HEADER_QUEUE_NAME, true, false, false);
    }

    @Bean
    Queue headerAgeQueue() {
        return new Queue(HEADER_QUEUE_AGE, true, false, false);
    }

    @Bean
    HeadersExchange headersExchange() {
        return new HeadersExchange(HEADER_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding nameBinding() {
        return BindingBuilder.bind(headerNameQueue())
                .to(headersExchange())
                //如果将来消息头部中包含 name 属性，就算匹配成功
                .where("name").exists();
    }

    @Bean
    public Binding ageBinding() {
        return BindingBuilder.bind(headerAgeQueue())
                .to(headersExchange())
                //将来头信息中必须要有 age 属性，并且 age 属性值为 99
                .where("age")
                .matches(99); // 注意数据类型也要匹配
    }
}
