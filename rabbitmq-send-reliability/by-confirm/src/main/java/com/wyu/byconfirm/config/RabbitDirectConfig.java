package com.wyu.byconfirm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.PostConstruct;

/**
 * DirectExchange 的路由策略是将消息队列绑定到一个 DirectExchange 上
 * 当一条消息到达 DirectExchange 时会被转发到与该条消息 routing key 相同的 Queue 上
 *
 * @author novo
 * @since 2023-03-17
 */
@Configuration
@Slf4j
public class RabbitDirectConfig {

    public static final String DIRECT_QUEUE_0 = "direct_queue_0";

    public static final String DIRECT_EXCHANGE_NAME = "direct_exchange";

    @Bean
    public Queue queue0() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(DIRECT_QUEUE_0, true, false, false);
    }

    /**
     * 直连交换机
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        // 这里配置的持久化是交换机本身能否被持久化而不是消息 消息只能在队列里持久化
        // 重启应用后交换机是否还存在
        // autoDelete:没有与之绑定的队列，是否删除交换机
        return new DirectExchange(DIRECT_EXCHANGE_NAME, false, false);
    }

    /**
     * 将队列和交换机绑定
     *
     * @return
     */
    @Bean
    Binding directBinding0() {
        return BindingBuilder.bind(queue0())
                .to(directExchange())
                .with(DIRECT_QUEUE_0); // 设置routing_key 这里直接用队列名字
    }

    /**
     * 自己创建bean的话 properties文件中rabbitmq的配置会失效
     * 因为配置文件里的配置是用于SpringBoot自动装配的 并不会应用在我们自己创建的bean上面
     * 可以debug看出来
     *
     * @param factory
     * @return
     */
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
//        /**
//         * 注意如果自己new RabbitTemplate要设置setMandatory(true) 否则无法触发MessageReturn
//         * Mandatory为true时,消息通过交换器无法匹配到队列会返回给生产者
//         * 并触发MessageReturn
//         * 为false时,匹配不到会直接被丢弃
//         */
//        rabbitTemplate.setMandatory(true);
//        // 设置消息发送确认回调函数
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                if (ack) {
//                    log.info("correlation_id:[{}],消息成功到达交换机", correlationData.getId());
//                } else {
//                    log.error("correlation_id:[{}],消息未到达交换机,cause:[{}]", correlationData.getId(), cause);
//                    // TODO 保证了消息发送的可靠性，发送失败如果不是代码问题，可以在这里处理失败逻辑
//                }
//            }
//        });
//        // 设置消息发送失败回调函数
//        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
//            @Override
//            public void returnedMessage(ReturnedMessage returned) {
//                log.error("data:[{}]", returned);
//                Message message = returned.getMessage();
//                String correlationId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
//                log.error("correlation_id:[{}],消息未到达队列,message:[{}]", correlationId, new String(message.getBody()));
//            }
//        });
//        return rabbitTemplate;
//    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 解决方案：我们可以在SpringBoot提供的Template上追加配置
     */
    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    log.info("correlation_id:[{}],消息成功到达交换机", correlationData.getId());
                } else {
                    log.error("correlation_id:[{}],消息未到达交换机,cause:[{}]", correlationData.getId(), cause);
                    // TODO 保证了消息发送的可靠性，发送失败如果不是代码问题，可以在这里处理失败逻辑
                }
            }
        });
        // 设置消息发送失败回调函数
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.error("data:[{}]", returned);
                Message message = returned.getMessage();
                String correlationId = message.getMessageProperties().getHeader("spring_returned_message_correlation");
                log.error("correlation_id:[{}],消息未到达队列,message:[{}]", correlationId, new String(message.getBody()));
            }
        });
    }
}
