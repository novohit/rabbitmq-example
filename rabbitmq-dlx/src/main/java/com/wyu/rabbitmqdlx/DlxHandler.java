package com.wyu.rabbitmqdlx;

import com.wyu.rabbitmqdlx.config.RabbitDlxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author novo
 * @since 2023-03-18
 */
@Component
public class DlxHandler {

    private static final Logger logger = LoggerFactory.getLogger(DlxHandler.class);

    /**
     * 实际上就是普通的消费者
     * 用来处理死信消息
     */
    @RabbitListener(queues = RabbitDlxConfig.DEAD_LETTER_QUEUE)
    public void dlxHandler(String msg) {
        // 处理死信
        logger.info(msg);
    }
}
