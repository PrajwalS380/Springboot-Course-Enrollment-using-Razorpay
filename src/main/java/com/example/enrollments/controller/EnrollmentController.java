package com.example.enrollments.controller;

import com.example.enrollments.entity.Enrollment;
import com.example.enrollments.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enroll")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    // Create pending enrollment and return razorpay order id and enrollment id
    @PostMapping("/{courseId}")
    public Map<String, Object> enroll(@RequestParam Long userId, @PathVariable Long courseId) throws Exception {
        Enrollment en = enrollmentService.createPendingEnrollment(userId, courseId);
        return Map.of(
                "enrollmentId", en.getId(),
                "razorpayOrderId", en.getRazorpayOrderId(),
                "amount", en.getCourse().getPrice()
        );
    }

    @GetMapping("/all")
    public List<Enrollment> allEnrollments() { return enrollmentService.getAll(); }

    @GetMapping("/user/{userId}")
    public List<Enrollment> byUser(@PathVariable Long userId) { return enrollmentService.getByUser(userId); }
}
