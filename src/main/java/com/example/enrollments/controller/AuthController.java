package com.example.enrollments.controller;

import com.example.enrollments.entity.User;
import com.example.enrollments.service.AuthService;
import com.example.enrollments.dto.AuthDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody AuthDtos.RegisterRequest req) {
        return authService.register(req.getName(), req.getEmail(), req.getPassword());
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        boolean ok = authService.verify(token);
        return ok ? "Verified" : "Invalid token";
    }

    @PostMapping("/login")
    public User login(@RequestBody AuthDtos.LoginRequest req) {
        return authService.login(req.getEmail(), req.getPassword());
    }
}
