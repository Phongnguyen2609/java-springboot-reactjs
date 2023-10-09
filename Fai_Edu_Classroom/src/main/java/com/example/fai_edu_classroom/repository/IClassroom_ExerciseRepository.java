package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.relationship.Classroom_Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IClassroom_ExerciseRepository extends JpaRepository<Classroom_Exercise, Long> {
    List<Classroom_Exercise> findClassroom_ExercisesByClassroomId(Long id);
    List<Classroom_Exercise> findClassroom_ExercisesByExerciseId(Long id);

    List<Classroom_Exercise> findClassroom_ExercisesByClassroomIdAndStatus(Long id, String status);


    Optional<Classroom_Exercise> findClassroom_ExerciseByClassroomId(Long id);
    Optional<Classroom_Exercise> findClassroom_ExerciseByExerciseId(Long id);

    Optional<Classroom_Exercise> findClassroom_ExerciseByClassroomIdAndStatus(Long id, String status);

}
