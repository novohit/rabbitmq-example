package com.wyu.rabbitmqbizreliability.task;

import com.wyu.rabbitmqbizreliability.config.RabbitConfig;
import com.wyu.rabbitmqbizreliability.enums.MsgSendStatus;
import com.wyu.rabbitmqbizreliability.model.MsgSendLog;
import com.wyu.rabbitmqbizreliability.repository.SendLogRepository;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author novo
 * @since 2023-03-21
 */
@Component
public class MsgSendTask {

    @Autowired
    private SendLogRepository sendLogRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void msgRetryTask() {
        List<MsgSendLog> retryMsgs = this.sendLogRepository.findByStatusAndTryTimeBefore(MsgSendStatus.SENDING.name(), new Date());
        retryMsgs.forEach(msgSendLog -> {
            if (msgSendLog.getCount() >= 3) {
                // 重试次数大于等于3 认为该消息已经发送失败，需要告警，进行人工处理
                this.sendLogRepository.updateStatusByMsgId(msgSendLog.getMsgId(), MsgSendStatus.FAIL.name());
                // TODO 告警
            } else {
                // 开始重试 向mq重新发送消息
                msgSendLog.setCount(msgSendLog.getCount() + 1);
                msgSendLog.setNotQueue(false);
                this.sendLogRepository.save(msgSendLog);
                this.rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_EXCHANGE_NAME, RabbitConfig.DIRECT_QUEUE_0, msgSendLog.getEmail(), new CorrelationData(msgSendLog.getMsgId()));
            }
        });
    }

}
