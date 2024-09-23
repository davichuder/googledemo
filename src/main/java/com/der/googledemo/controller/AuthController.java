package com.der.googledemo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController("/")
public class AuthController {
    @PostMapping("register")
    public String register(@RequestParam String email, @RequestParam String password) {
        // TODO
        return "ok";
    }
    
    @PostMapping("login")
    public String login(@RequestParam String email, @RequestParam String password) {
        // TODO
        return "ok";
    }

    @GetMapping("login-with-google")
    public String loginWithGoogle() {
        // TODO
        return "ok";
    }
}
