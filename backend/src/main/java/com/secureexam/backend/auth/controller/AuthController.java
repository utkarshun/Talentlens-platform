package com.secureexam.backend.auth.controller;

import com.secureexam.backend.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.secureexam.backend.auth.dto.AuthResponse;
import com.secureexam.backend.auth.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody com.secureexam.backend.auth.dto.RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
