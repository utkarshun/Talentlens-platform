package com.secureexam.backend.test.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String hello(){
        return "ADMIN only endpoint working";
    }
}
