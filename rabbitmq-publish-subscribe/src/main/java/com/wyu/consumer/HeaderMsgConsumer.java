package com.wyu.consumer;

import com.wyu.config.RabbitHeaderConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author novo
 * @since 2023-03-17
 */
@Component
@Slf4j
public class HeaderMsgConsumer {

    /**
     * amqp和springboot的Message对象都可以用
     *
     * @param message
     */
    @RabbitListener(queues = RabbitHeaderConfig.HEADER_QUEUE_AGE)
    public void handleMsg1(Message message) {
        log.info("header_with_age_queue msg:[{}] headers:[{}]", new String((byte[]) message.getPayload()), message.getHeaders());
    }


    @RabbitListener(queues = RabbitHeaderConfig.HEADER_QUEUE_NAME)
    public void handleMsg2(Message message) {
        log.info("header_with_name_queue msg:[{}] headers:[{}]", new String((byte[]) message.getPayload()), message.getHeaders());
    }
}
