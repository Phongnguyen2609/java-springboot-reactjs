package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByClassroomName(String name);
    boolean existsByCode(String name);

    Optional<Classroom> findByCode(String code);
}
