package com.wyu.client.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author novo
 * @since 2023-03-18
 */
@Configuration
public class RabbitConfig {

    // 存放客户端发送消息的队列
    public static final String RPC_MSG_QUEUE = "rpc_msg_queue";

    // 存放服务端响应消息的队列
    public static final String RPC_REPLY_MSG_QUEUE = "rpc_reply_msg_queue";

    public static final String RPC_EXCHANGE = "RPC_EXCHANGE";

    @Bean
    public Queue rpcMsgQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(RPC_MSG_QUEUE, true, false, false);
    }

    @Bean
    public Queue rpcReplyMsgQueue() {
        // durable是否持久化
        // exclusive是否排他性 队列由谁(Connection)创建只能由谁消费
        // autoDelete 没有消费者是否自动删除该队列
        return new Queue(RPC_REPLY_MSG_QUEUE, true, false, false);
    }

    @Bean
    public DirectExchange directExchange() {
        // 这里配置的持久化是交换机本身能否被持久化而不是消息 消息只能在队列里持久化
        // 重启应用后交换机是否还存在
        // autoDelete:没有与之绑定的队列，是否删除交换机
        return new DirectExchange(RPC_EXCHANGE, false, false);
    }

    /**
     * 将队列和交换机绑定
     *
     * @return
     */
    @Bean
    Binding directBinding0() {
        return BindingBuilder.bind(rpcMsgQueue())
                .to(directExchange())
                .with(RPC_MSG_QUEUE); // 设置routing_key 这里直接用队列名字
    }

    @Bean
    Binding directBinding1() {
        return BindingBuilder.bind(rpcReplyMsgQueue())
                .to(directExchange())
                .with(RPC_REPLY_MSG_QUEUE); // 设置routing_key 这里直接用队列名字
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setReplyAddress(RPC_REPLY_MSG_QUEUE); // 设置回调队列 也就是接收响应消息的队列
        //rabbitTemplate.setUserCorrelationId(true);
        rabbitTemplate.setReplyTimeout(6000); // 接收响应消息超时时间
        return rabbitTemplate;
    }


    /**
     * 给返回队列设置监听器
     * 为什么不像直接那样使用注解@RabbitListener监听
     * 因为rpc调用就是要做到像本地调用那样调用远程的函数，显示监听不够优雅，我们希望发送消息后可以立马得到一个响应结果
     */
    @Bean
    SimpleMessageListenerContainer replyContainer(ConnectionFactory factory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.setQueueNames(RPC_REPLY_MSG_QUEUE);
        container.setMessageListener(rabbitTemplate(factory));
        return container;
    }
}
