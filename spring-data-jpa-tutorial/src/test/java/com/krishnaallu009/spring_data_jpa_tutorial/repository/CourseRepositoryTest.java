package com.krishnaallu009.spring_data_jpa_tutorial.repository;

import com.krishnaallu009.spring_data_jpa_tutorial.entity.Course;
import com.krishnaallu009.spring_data_jpa_tutorial.entity.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CourseRepositoryTest {
    private final CourseRepository courseRepository;

    @Autowired
    CourseRepositoryTest(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Test
    public void saveCourseWithTeacher() {
        Teacher teacher = Teacher.builder()
                .firstName("Lakshmi")
                .lastName("K")
                .build();
        Course course = Course.builder()
                .title("Operating Systems")
                .credit(4)
                .teacher(teacher)
                .build();
        courseRepository.save(course);
    }

    @Test
    public void printCourses() {
        List<Course> courses = courseRepository.findAll();
        System.out.println("Courses: " + courses);
    }
}