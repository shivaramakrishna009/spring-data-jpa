package com.krishnaallu009.spring_data_jpa_tutorial.repository;

import com.krishnaallu009.spring_data_jpa_tutorial.entity.Guardian;
import com.krishnaallu009.spring_data_jpa_tutorial.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void saveStudent() {
        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .emailId("john.doe@gmail.com")
//                .guardianName("Jane Doe")
//                .guardianEmail("jane.doe@gmail.com")
//                .guardianMobile("1234567890")
                .build();
        studentRepository.save(student);
    }

    @Test
    public void saveStudentWithGuardian() {
        Guardian guardian = Guardian.builder()
                .name("Bhanu")
                .email("bhanu@gmail.com")
                .mobile("9876543210")
                .build();
        Student student = Student.builder()
                .firstName("Krishna")
                .lastName("Allu")
                .emailId("krishnaallu@gmail.com")
                .guardian(guardian)
                .build();

        studentRepository.save(student);
    }

    @Test
    public void printAllStudents() {
        System.out.println(studentRepository.findAll());
    }
}