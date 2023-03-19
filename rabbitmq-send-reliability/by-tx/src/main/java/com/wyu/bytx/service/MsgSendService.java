package com.wyu.bytx.service;

import com.wyu.bytx.config.RabbitDirectConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.RabbitExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author novo
 * @since 2023-03-19
 */
@Service
public class MsgSendService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 当我们开启事务模式之后，RabbitMQ 生产者发送消息会多出四个步骤：
     * 1.客户端发出请求，将信道设置为事务模式。
     * 2.服务端给出回复，同意将信道设置为事务模式。
     * 3.客户端发送消息。
     * 4.客户端提交事务。
     * 5.服务端给出响应，确认事务提交。
     * 上面的步骤，除了第三步是本来就有的，其他几个步骤都是平白无故多出来的。
     * 事务模式其实效率有点低，这并非一个最佳解决方案。
     * 我们可以想想，什么项目会用到消息中间件？一般来说都是一些高并发的项目，这个时候并发性能尤为重要。
     * 所以，RabbitMQ 还提供了发送方确认机制（publisher confirm）来确保消息发送成功，这种方式，性能要远远高于事务模式
     */
    @Transactional
    public void send() {
        // 开启事务模式 或者@Bean 自己提供RabbitTemplate 全局设置开启事务模式
        this.rabbitTemplate.setChannelTransacted(true);
        this.rabbitTemplate.convertAndSend(RabbitDirectConfig.DIRECT_EXCHANGE_NAME, RabbitDirectConfig.DIRECT_QUEUE_0, "hello");
        int i = 1 / 0;
    }
}
