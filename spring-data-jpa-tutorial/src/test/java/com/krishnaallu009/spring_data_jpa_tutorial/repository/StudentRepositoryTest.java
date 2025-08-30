package com.krishnaallu009.spring_data_jpa_tutorial.repository;

import com.krishnaallu009.spring_data_jpa_tutorial.entity.Guardian;
import com.krishnaallu009.spring_data_jpa_tutorial.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @Test
    public void printStudentsByFirstName() {
        List<Student> students = studentRepository.findByFirstName("Krishna");
        System.out.println("Students = " + students);
    }

    @Test
    public void printStudentsByFirstNameContaining() {
        List<Student> students = studentRepository.findByFirstNameContaining("ish");
        System.out.println("Students = " + students);
    }

    @Test
    public void printStudentsBasedOnGuardianName() {
        List<Student> students = studentRepository.findByGuardianName("Bhanu");
        System.out.println("Students = " + students);
    }

    @Test
    public void printGetStudentByEmailAddress() {
        String firstName = studentRepository.getStudentFirstNameByEmailAddress("john.doe@gmail.com");
        System.out.println("Student first name = " + firstName);
    }

    @Test
    public void printGetStudentByEmailAddressNative(){
        Student student = studentRepository.getStudentByEmailAddressNative("krishnaallu@gmail.com");
        System.out.println("Student = " + student);
    }

    @Test
    public void printGetStudentByEmailAddressNativeNamedParam() {
        Student student = studentRepository.getStudentByEmailAddressNativeNamedParam("krishnaallu@gmail.com");
        System.out.println("Student = " + student);
    }

    @Test
    public void updateStudentNameByEmailIdTest() {
        int rowsUpdated = studentRepository.updateStudentNameByEmailId("Siva ram", "krishnaallu@gmail.com");
        System.out.println("Number of rows updated: " + rowsUpdated);
    }

}