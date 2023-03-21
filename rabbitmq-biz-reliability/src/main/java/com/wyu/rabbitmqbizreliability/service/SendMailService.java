package com.wyu.rabbitmqbizreliability.service;

import com.wyu.rabbitmqbizreliability.config.RabbitConfig;
import com.wyu.rabbitmqbizreliability.enums.MsgSendStatus;
import com.wyu.rabbitmqbizreliability.model.MsgSendLog;
import com.wyu.rabbitmqbizreliability.repository.SendLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author novo
 * @since 2023-03-21
 */
@Service
@Slf4j
public class SendMailService {

    @Autowired
    private SendLogRepository sendLogRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 我们保证的是RabbitMQ消息发送的可靠性，而不是邮箱发送
     */
    public void sendMail() {
        String email = (new Random().nextInt(999) + 100) + "@gmail.com";
        Long bizId = (long) new Random().nextInt(999999) + 100000000;
        String msgId = UUID.randomUUID().toString();
        MsgSendLog msgSendLog = new MsgSendLog();
        msgSendLog.setStatus(MsgSendStatus.SENDING.name()); // 消息发送中
        msgSendLog.setEmail(email);
        msgSendLog.setCount(0);
        msgSendLog.setMsgId(msgId);
        msgSendLog.setBizId(bizId);
        msgSendLog.setExchange(RabbitConfig.DIRECT_EXCHANGE_NAME);
        msgSendLog.setRoutingKey(RabbitConfig.DIRECT_QUEUE_0);
        msgSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60));
        // 1.向数据库添加一条消息发送的日志
        this.sendLogRepository.save(msgSendLog);

        // 2.发送消息给mq 异步发送邮件
        this.rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE_NAME, RabbitConfig.DIRECT_QUEUE_0, email, new CorrelationData(msgId));
    }
}
