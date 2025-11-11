package com.example.enrollments.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import com.razorpay.RazorpayClient;

@Configuration
public class RazorpayConfig {
    @Value("${razorpay.keyId}") private String keyId;
    @Value("${razorpay.keySecret}") private String keySecret;

    @PostConstruct
    public void init() throws Exception {
        new RazorpayClient(keyId, keySecret); // just ensure credentials work on startup
    }
}
