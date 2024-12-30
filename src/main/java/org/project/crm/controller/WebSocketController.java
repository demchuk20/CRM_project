package org.project.crm.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/update")
    @SendTo("/topic/updates")
    public String sendMessage(String message) {
        return message;
    }

}
