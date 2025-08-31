package com.krishnaallu009.spring_data_jpa_tutorial.repository;

import com.krishnaallu009.spring_data_jpa_tutorial.entity.Course;
import com.krishnaallu009.spring_data_jpa_tutorial.entity.Teacher;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TeacherRepositoryTest {

    private final TeacherRepository teacherRepository;

    @Autowired
    TeacherRepositoryTest(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Test
    public void saveTeacher(){
        Course courseDbms = Course.builder()
                .title("DBMS")
                .credit(4)
                .build();

        Course courseJava = Course.builder()
                .title("Java")
                .credit(3)
                .build();

        Teacher teacher = Teacher.builder()
                .firstName("Bhargavi")
                .lastName("Paka")
//                .courses(List.of(courseDbms, courseJava))
//                Above line is commented to avoid cascading issue while saving teacher without courses
//                As we removed the OneToMany mapping from Teacher entity
                .build();
        teacherRepository.save(teacher);
    }

    @Test
    @Transactional
    public void printTeachers(){
        List<Teacher> teachers = teacherRepository.findAll();
        System.out.println("Teachers: " + teachers);
    }

}