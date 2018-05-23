package com.b2mark.kyc;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationCheck {

    @RequestMapping("/")
    public String index() {
        return "test this file";
    }

}
