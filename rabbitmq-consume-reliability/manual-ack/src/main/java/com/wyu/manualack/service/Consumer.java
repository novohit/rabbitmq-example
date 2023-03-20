package com.wyu.manualack.service;

import com.rabbitmq.client.Channel;
import com.wyu.manualack.config.RabbitDirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author novo
 * @since 2023-03-20
 */
@Component
@Slf4j
public class Consumer {

//    @RabbitListener(queues = RabbitDirectConfig.DIRECT_QUEUE_0)
//    public void msgHandler(Message message, Channel channel) {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        String msg = new String(message.getBody());
//        log.info("receive msg:[{}]", msg);
//        // 接收到消息后 抛出异常会一直循环接收消息 因为异常后 默认策略nack nack并把该消息重新放回队列
//        int i = 1 / 0;
//    }

    @RabbitListener(queues = RabbitDirectConfig.DIRECT_QUEUE_0)
    public void msgHandler2(Message message, Channel channel) {
        // 获取消息消费的标记
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        log.info("delivery_tag:[{}]", deliveryTag);
        try {
            String msg = new String(message.getBody());
            log.info("receive msg:[{}]", msg);
            // 手动ack multiple:是否批量ack 将之前unacked的消息也ack
            //int i = 1 / 0;
            channel.basicAck(deliveryTag, false); // ack语句放最后
        } catch (Exception e) {
            // requeue:是否将消息重新入队
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            log.error(e.getMessage());
        }
    }
}
