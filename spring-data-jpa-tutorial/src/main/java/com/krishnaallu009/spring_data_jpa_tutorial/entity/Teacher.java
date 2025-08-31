package com.krishnaallu009.spring_data_jpa_tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "courses")
public class Teacher {
    @Id
    @SequenceGenerator(
            name = "teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private long teacherId;
    private String firstName;
    private String lastName;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "teacher_id",              // FK column in child table
            referencedColumnName = "teacherId" // PK column in parent table
    )
    private List<Course> courses;
}
