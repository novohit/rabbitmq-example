package com.wyu.messagettl;

import com.wyu.messagettl.config.RabbitConfig;
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
    public void hello(String message) {
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8))
                .setExpiration(String.valueOf(10 * 1000)) // 设置10s过期时间
                .build();
        this.rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE, RabbitConfig.MSG_QUEUE, msg);
    }
}
