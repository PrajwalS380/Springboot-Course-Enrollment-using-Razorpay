package com.example.enrollments.service;

import com.example.enrollments.entity.Course;
import com.example.enrollments.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepo;

    public List<Course> all() { return courseRepo.findAll(); }
    public Course get(Long id) { return courseRepo.findById(id).orElseThrow(); }
    public Course create(Course c) { return courseRepo.save(c); }
    public Course update(Long id, Course c) {
        Course ex = courseRepo.findById(id).orElseThrow();
        ex.setTitle(c.getTitle());
        ex.setDescription(c.getDescription());
        ex.setPrice(c.getPrice());
        return courseRepo.save(ex);
    }
    public void delete(Long id) { courseRepo.deleteById(id); }
}
