package com.example.enrollments.service;

import com.example.enrollments.entity.User;
import com.example.enrollments.repository.UserRepository;
import com.example.enrollments.util.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public User register(String name, String email, String password) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) throw new IllegalArgumentException("Email already taken");

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(PasswordEncoderUtil.encode(password));
        u.setVerified(false);
        u.setVerificationToken(UUID.randomUUID().toString());
        userRepository.save(u);
        emailService.sendVerificationEmail(email, u.getVerificationToken());
        return u;
    }

    public boolean verify(String token) {
        return userRepository.findByVerificationToken(token)
                .map(u -> {
                    u.setVerified(true);
                    u.setVerificationToken(null);
                    userRepository.save(u);
                    return true;
                }).orElse(false);
    }

    public User login(String email, String password) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!PasswordEncoderUtil.matches(password, u.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");
        if (!u.isVerified()) throw new IllegalStateException("Email not verified");
        return u;
    }
}
