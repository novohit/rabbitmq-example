package com.wyu.rabbitmqbizreliability.consumer;

import com.wyu.rabbitmqbizreliability.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author novo
 * @since 2023-03-21
 */
@Component
@Slf4j
public class MailConsumer {

    @RabbitListener(queues = RabbitConfig.DIRECT_QUEUE_0)
    public void emailMsgHandler(String msg) {
        // 发送邮件业务逻辑
        log.info("发送邮箱" + msg);
    }
}
