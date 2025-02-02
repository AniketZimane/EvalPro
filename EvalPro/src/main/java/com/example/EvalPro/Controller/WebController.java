package com.example.EvalPro.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/userrole")
    public String getUserRolePage() {
        return "userTypes";
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "Registration";
    }

    @GetMapping("/forgetpassword")
    public String getForgetPasswordPage() {
        return "forgetpassword";
    }
}
