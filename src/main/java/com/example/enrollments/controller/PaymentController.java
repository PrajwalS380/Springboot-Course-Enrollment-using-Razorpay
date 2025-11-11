package com.example.enrollments.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enrollments.service.EnrollmentService;
import com.example.enrollments.service.RazorpayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final RazorpayService razorpayService;
    private final EnrollmentService enrollmentService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> paymentData) {
        String orderId = paymentData.get("razorpay_order_id");
        String paymentId = paymentData.get("razorpay_payment_id");
        String signature = paymentData.get("razorpay_signature");

        try {
            boolean isValid = razorpayService.verifySignature(orderId, paymentId, signature);
            if (isValid) {
                enrollmentService.markPaid(orderId, paymentId);
                return ResponseEntity.ok("✅ Payment verified successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("❌ Invalid signature verification");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error verifying payment: " + e.getMessage());
        }
    }

    
    @PostMapping("/mock")
    public ResponseEntity<?> simulatePayment(@RequestParam String orderId) {
        try {
            JSONObject result = razorpayService.simulatePayment(orderId);
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error simulating payment: " + e.getMessage());
        }
    }

}
