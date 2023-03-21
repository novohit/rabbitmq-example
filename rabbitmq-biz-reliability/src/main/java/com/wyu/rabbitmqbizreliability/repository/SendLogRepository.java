package com.wyu.rabbitmqbizreliability.repository;

import com.wyu.rabbitmqbizreliability.model.MsgSendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author novo
 * @since 2023-03-21
 */
public interface SendLogRepository extends JpaRepository<MsgSendLog, Long> {

    List<MsgSendLog> findByStatusAndTryTimeBefore(String status, Date now);

    /**
     * 两个注解要分开写@Entity @Table(name = "msg_send_log")
     * 如果@Entity(name = "msg_send_log")合在一起 这里的jpql会识别不了model
     *
     * @param msgId
     * @param status
     * @return
     */
    @Transactional
    @Modifying
    @Query("update MsgSendLog as m\n" +
            "set m.status = :status\n" +
            "where m.msgId = :msgId\n")
    int updateStatusByMsgId(String msgId, String status);


    /**
     * 因为returnCallBack回调是先执行，要判断是否没进入队列，进入队列的才能修改
     *
     * @param msgId
     * @param status
     * @return
     */
    @Transactional
    @Modifying
    @Query("update MsgSendLog as m\n" +
            "set m.status = :status\n" +
            "where m.msgId = :msgId\n" +
            "and m.notQueue = false ")
    int updateStatusByMsgIdNotQueue(String msgId, String status);

    @Transactional
    @Modifying
    @Query("update MsgSendLog as m\n" +
            "set m.status = :status,\n" +
            "m.notQueue = :notQueue\n" +
            "where m.msgId = :msgId")
    int updateStatusByMsgId(String msgId, String status, Boolean notQueue);
}
