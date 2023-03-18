package com.wyu.producer;

import com.wyu.config.RabbitDirectConfig;
import com.wyu.config.RabbitFanoutConfig;
import com.wyu.config.RabbitTopicConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TopicExchange 是比较复杂但是也比较灵活的一种路由策略，在 TopicExchange 中，Queue 通过 routingkey 绑定到 TopicExchange 上，
 * 当消息到达 TopicExchange 后，TopicExchange 根据消息的 routingkey 将消息路由到一个或者多个 Queue 上
 *
 * @author novo
 * @since 2023-03-17
 */
@SpringBootTest
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void sendMsgTest() {
        // (String exchange, String routingKey, Object object)
        // 带上routing_key发送消息到交换机
        this.rabbitTemplate.convertAndSend(RabbitDirectConfig.DIRECT_EXCHANGE_NAME, RabbitDirectConfig.DIRECT_QUEUE_0, "消息发给队列0");
        this.rabbitTemplate.convertAndSend(RabbitDirectConfig.DIRECT_EXCHANGE_NAME, RabbitDirectConfig.DIRECT_QUEUE_1, "消息发给队列1");
    }

    @Test
    void sendMsgToFanoutExchangeTest() {
        this.rabbitTemplate.convertAndSend(RabbitFanoutConfig.FANOUT_EXCHANGE_NAME, null, "hello fanout");
    }

    @Test
    void sendMsgToTopicExchangeTest() {
//        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, "huawei.furniture", "华为家居");
//        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, "xiaomi.furniture", "小米家居");
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, "huawei.phone.new", "华为手机新闻");
    }
}
