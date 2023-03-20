package com.wyu.manualack.controller;

import com.wyu.manualack.service.MsgSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author novo
 * @since 2023-03-19
 */
@RestController
public class SendController {

    @Autowired
    private MsgSendService msgSendService;

    @GetMapping("/send")
    public void send() {
        this.msgSendService.send();
    }

    @GetMapping("/pull")
    public Object pull() {
        return this.msgSendService.pull();
    }
}
