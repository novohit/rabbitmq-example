package com.wyu.rabbitmqbizreliability.config;

import com.wyu.rabbitmqbizreliability.enums.MsgSendStatus;
import com.wyu.rabbitmqbizreliability.model.MsgSendLog;
import com.wyu.rabbitmqbizreliability.repository.SendLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author novo
 * @since 2023-03-21
 */
@Configuration
@Slf4j
public class RabbitConfig {

    public static final String DIRECT_QUEUE_0 = "direct_queue_0";

    public static final String DIRECT_EXCHANGE_NAME = "direct_exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SendLogRepository sendLogRepository;

    @PostConstruct
    public void initTemplate() {
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("correlation_id:[{}],消息成功到达交换机", correlationData.getId());
                // 到达交换机 就认为消息发送成功
                this.sendLogRepository.updateStatusByMsgIdNotQueue(correlationData.getId(), MsgSendStatus.SUCCESS.name());
            } else {
                log.error("correlation_id:[{}],消息未到达交换机,cause:[{}]", correlationData.getId(), cause);
            }
        });
        this.rabbitTemplate.setReturnsCallback(returned -> {
            log.error("data:[{}]", returned);
            Message message = returned.getMessage();
            String correlationId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
            log.error("correlation_id:[{}],消息未到达队列,message:[{}]", correlationId, new String(message.getBody()));
            // 未到达队列 则需要重新发送消息
            this.sendLogRepository.updateStatusByMsgId(correlationId, MsgSendStatus.SENDING.name(), true);
        });
    }

    @Bean
    public Queue queue0() {
        return new Queue(DIRECT_QUEUE_0, true, false, false);
    }

    @Bean
    public DirectExchange directExchange0() {
        return new DirectExchange(DIRECT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue0())
                .to(directExchange0())
                .with(DIRECT_QUEUE_0);
    }


}
