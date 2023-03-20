package com.wyu.manualack.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import com.wyu.manualack.config.RabbitDirectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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

    /**
     * 消息消费有两种模式
     * 使用@RabbitListener注解就是推模式
     * 当监听的队列中有消息时，就会推送给客户端
     */
    public Object pull() {
        // 拉模式
        Object o = this.rabbitTemplate.receiveAndConvert(RabbitDirectConfig.DIRECT_QUEUE_0);
        assert o != null;
        log.info(o.toString());
        return o;
    }

    /**
     * 拉模式的手动ack SpringBoot没有封装 需要用原始的方法
     */
    public void pullManual() {
        Channel channel = rabbitTemplate.getConnectionFactory().createConnection().createChannel(false);
        long deliveryTag = 0L;
        try {
            GetResponse getResponse = channel.basicGet(RabbitDirectConfig.DIRECT_QUEUE_0, false);
            deliveryTag = getResponse.getEnvelope().getDeliveryTag();
            String msg = new String(getResponse.getBody());
            log.info(msg);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
