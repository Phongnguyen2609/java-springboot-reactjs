package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAccount_ExerciseRepository extends JpaRepository<Account_Exercise, Long> {
    List<Account_Exercise> findAccount_ExercisesByAccountId(Long id);
    List<Account_Exercise> findAccount_ExercisesByExerciseId(Long id);

    Optional<Account_Exercise> findAccount_ExerciseByAccountIdAndExerciseId(Long accountId, Long exerciseId);

}
