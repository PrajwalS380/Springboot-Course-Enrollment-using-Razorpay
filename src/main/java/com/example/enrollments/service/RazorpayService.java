package com.example.enrollments.service;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.enrollments.util.SignatureUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayService {
    @Value("${razorpay.keyId}") private String keyId;
    @Value("${razorpay.keySecret}") private String keySecret;
    private RazorpayClient client;

    @PostConstruct
    public void init() throws Exception {
        client = new RazorpayClient(keyId, keySecret);
    }

    public Order createOrder(double amountINR) throws Exception {
        JSONObject orderRequest = new JSONObject();
        int amountPaise = (int) Math.round(amountINR * 100);
        orderRequest.put("amount", amountPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);
        return client.Orders.create(orderRequest);
    }

    public boolean verifySignature(String orderId, String paymentId, String signature) throws Exception {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);

            com.razorpay.Utils.verifyPaymentSignature(attributes, keySecret);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public JSONObject simulatePayment(String orderId) throws Exception {
        // Normally, Razorpay sends these after user pays on frontend
        String paymentId = "pay_" + UUID.randomUUID().toString().substring(0, 10);
        
        // Generate fake signature using real logic (same as Razorpay would)
        String payload = orderId + "|" + paymentId;
        String generatedSignature = SignatureUtil.hmacSHA256(payload, keySecret);

        JSONObject fakeResponse = new JSONObject();
        fakeResponse.put("razorpay_order_id", orderId);
        fakeResponse.put("razorpay_payment_id", paymentId);
        fakeResponse.put("razorpay_signature", generatedSignature);

        return fakeResponse;
    }



}
