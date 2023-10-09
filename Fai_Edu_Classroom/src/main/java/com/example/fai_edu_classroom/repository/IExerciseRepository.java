package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExerciseRepository extends JpaRepository<Exercise, Long> {
}
