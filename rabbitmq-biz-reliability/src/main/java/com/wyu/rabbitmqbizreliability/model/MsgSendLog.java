package com.wyu.rabbitmqbizreliability.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author novo
 * @since 2023-03-20
 */
@Entity
@Table(name = "msg_send_log")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MsgSendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msgId;

    private String email;

    private Long bizId;

    /**
     * 消息发送状态 0发送中 1发送成功 2发送失败
     */
    private String status;

    private String routingKey;

    private String exchange;

    /**
     * 未到达队列的消息
     */
    private Boolean notQueue = false;

    /**
     * retry次数
     */
    private Integer count;

    private Date tryTime;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Date createTime;

    @LastModifiedDate
    @Column(nullable = false)
    private Date updateTime;
}
