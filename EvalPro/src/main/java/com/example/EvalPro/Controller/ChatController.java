package com.example.EvalPro.Controller;

import com.example.EvalPro.Service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:5500") // Allow frontend requests
public class ChatController {

    private final GeminiService geminiService;

    // Constructor Injection (Recommended)
    public ChatController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/ask")
    public @ResponseBody String chatWithGemini(@RequestBody String userMessage) {
        System.out.println("User message"+userMessage);
        return geminiService.getGeminiResponse(userMessage);
    }
}
