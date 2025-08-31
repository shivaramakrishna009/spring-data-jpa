package com.krishnaallu009.spring_data_jpa_tutorial.repository;

import com.krishnaallu009.spring_data_jpa_tutorial.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByTitleContaining(String title, PageRequest pageRequest);
}
