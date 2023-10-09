package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.relationship.Account_Comment_Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAccount_Comment_ExerciseRepository extends JpaRepository<Account_Comment_Exercise, Long> {
    List<Account_Comment_Exercise> findAccount_Comment_ExercisesByExerciseIdAndStatus(Long id, String status);

    List<Account_Comment_Exercise> findAccount_Comment_ExercisesByExerciseId(Long id);

    Optional<Account_Comment_Exercise> findAccount_Comment_ExerciseByCommentId(Long id);
}
