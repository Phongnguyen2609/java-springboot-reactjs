package com.example.fai_edu_classroom.entity.relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "classrooms_exercises")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Classroom_Exercise {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "classroom_id")
    private Long classroomId;

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Column(name = "max_point")
    private Double point;

    @Column(name = "deadline")
    private String deadline;

    @Column(name = "status")
    private String status;
}
