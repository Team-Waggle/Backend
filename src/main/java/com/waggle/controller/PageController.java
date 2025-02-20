package com.waggle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/login-processing")
    public String loginProcessingPage() {
        return "login-processing";
    }
}
