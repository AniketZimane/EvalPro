package com.example.EvalPro.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping("/")
    public String getHomePage() {
        return "Chatbot";  // Renders "Chatbot.html" from templates folder
    }
    @GetMapping("/dashboard/")
    public String getDashboardPage() {
        return "index";  // Renders "index.html" from templates folder
    }
}
