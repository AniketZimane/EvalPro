package com.example.EvalPro.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasterController {
    @GetMapping("/paper/analysis/")
    public String getAiAnlysisPage()
    {
        return "AiAnsManupulation";
    }
}
