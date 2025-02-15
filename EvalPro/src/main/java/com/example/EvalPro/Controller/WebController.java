package com.example.EvalPro.Controller;

import com.example.EvalPro.Dao.RegistrationRepo;
import com.example.EvalPro.Entity.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WebController {
    @Autowired
    RegistrationRepo registrationRepo;
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

    @PostMapping("/save/new/reg/")
    public String getRegistrationData(Model model, Registration reg)
    {
        String page="Registration";
        List<Registration> registrationList=registrationRepo.findAll();
        if(registrationList.equals(reg.getEmail()))
        {
            model.addAttribute("emsg","User already exist!");
            page="Registration";        }
        else{
            registrationRepo.save(reg);
            model.addAttribute("msg","Registration done!");
            page="login";
        }

        return page;
    }
}
