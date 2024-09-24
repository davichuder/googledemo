package com.der.googledemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/client")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String client() {
        return "client";
    }

    @GetMapping("/project-leader")
    @PreAuthorize("hasRole('ROLE_PROJECT_LEADER')")
    public String projectLeader() {
        return "project leader";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin() {
        return "admin";
    }
}
