package com.example.enrollments.dto;

import lombok.Data;

public class AuthDtos {
    @Data
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
