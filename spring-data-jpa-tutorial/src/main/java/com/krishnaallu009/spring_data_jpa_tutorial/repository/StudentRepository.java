package com.krishnaallu009.spring_data_jpa_tutorial.repository;

import com.krishnaallu009.spring_data_jpa_tutorial.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    public List<Student> findByFirstName(String firstName);

    public List<Student> findByFirstNameContaining(String name);

    public List<Student> findByLastNameNotNull();

    public List<Student> findByGuardianName(String guardianName);

    // JPQL
    @Query("select s.firstName from Student s where s.emailId = ?1")
    public String getStudentFirstNameByEmailAddress(String emailId);

    //Native Query
    @Query(
            value = "SELECT * FROM tbl_student s WHERE s.email_address = ?1",
            nativeQuery=true
    )
    public Student getStudentByEmailAddressNative(String emilId);

    //Native Named Param
    @Query(
            value = "SELECT * FROM tbl_student s WHERE s.email_address = :mailId",
            nativeQuery=true
    )
    public Student getStudentByEmailAddressNativeNamedParam(@Param("mailId") String emailId);

    //Update Query
    @Modifying
    @Transactional
    @Query(
            value = "UPDATE tbl_student SET first_name = :name WHERE email_address = :emailId",
            nativeQuery = true
    )
    public int updateStudentNameByEmailId(@Param("name") String firstName, String emailId);
}
