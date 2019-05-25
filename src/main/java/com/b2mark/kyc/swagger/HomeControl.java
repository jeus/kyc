package com.b2mark.kyc.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class HomeControl {
    @RequestMapping("/kyc/swagger")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}