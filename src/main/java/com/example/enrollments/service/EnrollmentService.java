package com.example.enrollments.service;

import com.example.enrollments.entity.Course;
import com.example.enrollments.entity.Enrollment;
import com.example.enrollments.entity.User;
import com.example.enrollments.repository.CourseRepository;
import com.example.enrollments.repository.EnrollmentRepository;
import com.example.enrollments.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final RazorpayService razorpayService;

    public Enrollment createPendingEnrollment(Long userId, Long courseId) throws Exception {
        User u = userRepo.findById(userId).orElseThrow();
        Course c = courseRepo.findById(courseId).orElseThrow();
        if (!u.isVerified()) throw new IllegalStateException("User not verified");

        Enrollment en = new Enrollment();
        en.setUser(u);
        en.setCourse(c);
        en.setPaymentStatus("PENDING");
        Enrollment saved = enrollmentRepo.save(en);

        // Create razorpay order
        var order = razorpayService.createOrder(c.getPrice());
        saved.setRazorpayOrderId(order.get("id"));
        return enrollmentRepo.save(saved);
    }

    public List<Enrollment> getAll() { return enrollmentRepo.findAll(); }
    public List<Enrollment> getByUser(Long userId) { return enrollmentRepo.findByUserId(userId); }

    public Enrollment markPaid(String razorpayOrderId, String paymentId) {
        Enrollment en = enrollmentRepo.findAll().stream()
                .filter(e -> razorpayOrderId.equals(e.getRazorpayOrderId()))
                .findFirst().orElseThrow();
        en.setPaymentStatus("PAID");
        en.setRazorpayPaymentId(paymentId);
        return enrollmentRepo.save(en);
    }
}
