package com.wyu.delaybyplugin;

import com.wyu.delaybyplugin.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

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
                .setHeader("x-delay", 5 * 1000)
                .build();
        this.rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, RabbitConfig.DELAY_QUEUE, message);
    }
}
