package com.wyu.rabbitmqbizreliability.controller;

import com.wyu.rabbitmqbizreliability.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author novo
 * @since 2023-03-21
 */
@RestController
public class SendController {

    @Autowired
    private SendMailService sendMailService;

    @GetMapping("/send")
    public void send() {
        this.sendMailService.sendMail();
    }
}
