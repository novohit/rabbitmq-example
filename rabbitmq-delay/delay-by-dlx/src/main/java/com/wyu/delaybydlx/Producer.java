package com.wyu.delaybydlx;

import com.wyu.delaybydlx.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author novo
 * @since 2023-03-18
 */
@RestController
@Slf4j
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public void sendDelay(String msg) {
        log.info("send time");
        Message message = MessageBuilder.withBody(msg.getBytes(StandardCharsets.UTF_8))
                .build();
        this.rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, RabbitConfig.DELAY_QUEUE, message);
    }
}
