package com.wyu.delaybydlx;

import com.wyu.delaybydlx.config.RabbitConfig;
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

    /**
     * 只需要监听死信队列
     * 消息发到普通队列后，过期后就会进入死信队列从而实现延迟消息处理
     *
     * @param message
     */
    @RabbitListener(queues = RabbitConfig.DEAD_LETTER_QUEUE)
    public void msgHandler(Message message) {
        log.info("receive time, msg:[{}]", new String(message.getBody()));
    }
}
