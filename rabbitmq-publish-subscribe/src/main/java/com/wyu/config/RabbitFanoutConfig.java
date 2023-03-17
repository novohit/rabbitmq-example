package com.wyu.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FanoutExchange 的数据交换策略是把所有到达 FanoutExchange 的消息转发给所有与它绑定的 Queue 上
 * 在这种策略中，routingkey 将不起任何作用，相当于于广播
 * Work Queues和Publish/Subscribe模式Fanout区别：
 * work queues是一个队列有多个消费者同时竞争
 * 而fanout是一个交换机广播到多个队列给多个消费者同时消费，没有竞争关系
 *
 * @author novo
 * @since 2023-03-17
 */
@Configuration
public class RabbitFanoutConfig {

    public static final String FANOUT_QUEUE_0 = "fanout_queue_0";

    public static final String FANOUT_QUEUE_1 = "fanout_queue_1";

    public static final String FANOUT_EXCHANGE_NAME = "fanout_exchange";

    @Bean
    Queue fanoutQueue0() {
        return new Queue(FANOUT_QUEUE_0, true, false, false);
    }

    @Bean
    Queue fanoutQueue1() {
        return new Queue(FANOUT_QUEUE_1, true, false, false);
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE_NAME, true, false);
    }


    @Bean
    Binding fanoutBinding0() {
        return BindingBuilder.bind(fanoutQueue0()).to(fanoutExchange());
    }

    @Bean
    Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }
}
