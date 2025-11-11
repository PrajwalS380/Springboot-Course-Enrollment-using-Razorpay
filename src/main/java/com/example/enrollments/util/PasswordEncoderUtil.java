package com.example.enrollments.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    private static final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    public static String encode(String raw) { return enc.encode(raw); }
    public static boolean matches(String raw, String hashed) { return enc.matches(raw, hashed); }
}
