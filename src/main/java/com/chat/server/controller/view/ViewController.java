package com.chat.server.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping
    public String conversation() {
        return "views/conversation";
    }

    @GetMapping("/login")
    public String login() {
        return "views/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "views/signup";
    }
}
