package com.example.EvalPro.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping("/")
    public String getHomePage() {
        return "Chatbot";  // Renders "Chatbot.html" from templates folder
    }
}
