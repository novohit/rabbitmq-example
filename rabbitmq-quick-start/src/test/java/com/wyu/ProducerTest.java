package com.wyu;

import com.wyu.config.RabbitConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author novo
 * @since 2023-03-17
 */
@SpringBootTest
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void sendMsgTest() {
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, i);
        }
    }
}
