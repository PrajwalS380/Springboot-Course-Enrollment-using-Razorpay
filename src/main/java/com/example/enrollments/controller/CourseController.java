package com.example.enrollments.controller;

import com.example.enrollments.entity.Course;
import com.example.enrollments.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @Value("${app.admin.token}")
    private String adminToken;

    @GetMapping
    public List<Course> all() { return courseService.all(); }

    @GetMapping("/{id}")
    public Course get(@PathVariable Long id) { return courseService.get(id); }

    // admin routes require header X-ADMIN-TOKEN
    @PostMapping
    public Course create(@RequestHeader("X-ADMIN-TOKEN") String token, @RequestBody Course c) {
        if (!adminToken.equals(token)) throw new SecurityException("Invalid admin token");
        return courseService.create(c);
    }

    @PutMapping("/{id}")
    public Course update(@RequestHeader("X-ADMIN-TOKEN") String token, @PathVariable Long id, @RequestBody Course c) {
        if (!adminToken.equals(token)) throw new SecurityException("Invalid admin token");
        return courseService.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-ADMIN-TOKEN") String token, @PathVariable Long id) {
        if (!adminToken.equals(token)) throw new SecurityException("Invalid admin token");
        courseService.delete(id);
    }
}
