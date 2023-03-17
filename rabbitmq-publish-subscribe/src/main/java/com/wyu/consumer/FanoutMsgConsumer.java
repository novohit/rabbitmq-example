package com.wyu.consumer;

import com.wyu.config.RabbitFanoutConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author novo
 * @since 2023-03-17
 */
@Component
@Slf4j
public class FanoutMsgConsumer {

    @RabbitListener(queues = RabbitFanoutConfig.FANOUT_QUEUE_0)
    public void msgHandler0(String msg) {
        log.info("consumer0 msg:[{}]", msg);
    }

    @RabbitListener(queues = RabbitFanoutConfig.FANOUT_QUEUE_1)
    public void msgHandler1(String msg) {
        log.info("consumer1 msg:[{}]", msg);
    }
}
