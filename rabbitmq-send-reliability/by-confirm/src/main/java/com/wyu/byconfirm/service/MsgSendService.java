package com.wyu.byconfirm.service;

import com.wyu.byconfirm.config.RabbitDirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author novo
 * @since 2023-03-19
 */
@Service
@Slf4j
public class MsgSendService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send() {
        // CorrelationData可以自己设置id 默认是uuid
        // 正确发送
        this.rabbitTemplate.convertAndSend(RabbitDirectConfig.DIRECT_EXCHANGE_NAME, RabbitDirectConfig.DIRECT_QUEUE_0, "hello", new CorrelationData());
        // 可以到达交换机 无法到达队列
        //this.rabbitTemplate.convertAndSend(RabbitDirectConfig.DIRECT_EXCHANGE_NAME, "xxx", "hello", new CorrelationData());
        // 无法达到交换机
        //this.rabbitTemplate.convertAndSend("xxx", RabbitDirectConfig.DIRECT_QUEUE_0, "hello", new CorrelationData());
    }
}
