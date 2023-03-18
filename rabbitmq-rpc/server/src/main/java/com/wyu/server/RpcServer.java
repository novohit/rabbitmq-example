package com.wyu.server;

import com.wyu.server.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author novo
 * @since 2023-03-18
 */
@Component
public class RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 提高并发数
     *
     * @param msg
     */
    @RabbitListener(queues = RabbitConfig.RPC_MSG_QUEUE, concurrency = "20")
    public void process(Message msg) {
        logger.info("server receive : {}", msg.toString());
        Message response = MessageBuilder.withBody(("i'm receive:" + new String(msg.getBody())).getBytes()).build();
        CorrelationData correlationData = new CorrelationData(msg.getMessageProperties().getCorrelationId());
        // 发送响应消息
        rabbitTemplate.sendAndReceive(RabbitConfig.RPC_EXCHANGE, RabbitConfig.RPC_REPLY_MSG_QUEUE, response, correlationData);
    }
}
