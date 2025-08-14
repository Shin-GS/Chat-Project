package com.chat.server.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String home() {
        return "views/home";
    }

    @GetMapping("/conversation")
    public String conversation() {
        return "views/conversation";
    }
}
