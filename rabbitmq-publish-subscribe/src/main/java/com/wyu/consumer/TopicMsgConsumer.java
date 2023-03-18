package com.wyu.consumer;

import com.wyu.config.RabbitTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author novo
 * @since 2023-03-17
 */
@Component
@Slf4j
public class TopicMsgConsumer {

    @RabbitListener(queues = RabbitTopicConfig.XIAOMI_QUEUE)
    public void xiaomiHandler(String msg) {
        log.info("xiaomi msg:[{}]", msg);
    }


    @RabbitListener(queues = RabbitTopicConfig.HUAWEI_QUEUE)
    public void huaweiHandler(String msg) {
        log.info("huawei msg:[{}]", msg);
    }

    @RabbitListener(queues = RabbitTopicConfig.PHONE_QUEUE)
    public void phoneHandler(String msg) {
        log.info("phone msg:[{}]", msg);
    }
}
