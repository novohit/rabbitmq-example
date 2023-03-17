package com.wyu.consumer;

import com.rabbitmq.client.Channel;
import com.wyu.config.RabbitDirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author novo
 * @since 2023-03-17
 */
@Component
@Slf4j
public class DirectMsgConsumer {

    @RabbitListener(queues = RabbitDirectConfig.DIRECT_QUEUE_0)
    public void handleMsg1(Message message, Channel channel) throws IOException {
        log.info("consumer1 msg:[{}] thread:[{}]", message.getPayload(), Thread.currentThread().getName());
        // 手动ack
        //channel.basicAck(((Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)), false);
    }


    @RabbitListener(queues = RabbitDirectConfig.DIRECT_QUEUE_1)
    public void handleMsg2(Message message, Channel channel) throws IOException {
        log.info("consumer2 msg:[{}] thread:[{}]", message.getPayload(), Thread.currentThread().getName());
        //channel.basicAck(((Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG)), false);
    }
}
