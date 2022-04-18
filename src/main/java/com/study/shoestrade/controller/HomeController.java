package com.study.shoestrade.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/member")
    public String member(){
        return "member";
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

}
