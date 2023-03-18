package com.wyu.delaybyplugin;

import com.wyu.delaybyplugin.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author novo
 * @since 2023-03-18
 */
@Component
@Slf4j
public class Consumer {

    @RabbitListener(queues = RabbitConfig.DELAY_QUEUE)
    public void msgHandler(Message message) {
        log.info("receive time, msg:[{}]", new String(message.getBody()));
    }
}
