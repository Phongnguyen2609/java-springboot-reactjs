package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.relationship.Account_Post_Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IAccount_Post_ExerciseRepository extends JpaRepository<Account_Post_Exercise, Long> {
    List<Account_Post_Exercise> findAccount_Post_ExercisesByAccountId(Long id);
    List<Account_Post_Exercise> findAccount_Post_ExercisesByExerciseId(Long id);

    Optional<Account_Post_Exercise> findAccount_Post_ExerciseByAccountIdAndExerciseId(Long accountId, Long exerciseId);

//    boolean existsAccount_Post_ExerciseBy
}
