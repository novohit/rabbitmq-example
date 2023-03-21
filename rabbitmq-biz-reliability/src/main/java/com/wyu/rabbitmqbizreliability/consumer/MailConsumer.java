package com.wyu.rabbitmqbizreliability.consumer;

import com.rabbitmq.client.Channel;
import com.wyu.rabbitmqbizreliability.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author novo
 * @since 2023-03-21
 */
@Component
@Slf4j
public class MailConsumer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitConfig.DIRECT_QUEUE_0)
    public void emailMsgHandler(Message message, Channel channel) {
        String msg = new String(message.getBody());
        String msgId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
        String value = redisTemplate.opsForValue().get("sended_mail.msg_id:" + msgId);
        // 解决消息幂等性问题 即防止消息重复消费
        if (value != null && !"".equals(value)) {
            log.info("消息已被消费");
        } else {
            // 发送邮件业务逻辑
            log.info("发送邮箱" + msg);
            redisTemplate.opsForValue().set("sended_mail.msg_id:" + msgId, msg, 1, TimeUnit.DAYS);
        }
    }
}
