package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.relationship.Account_Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAccount_ClassroomRepository extends JpaRepository<Account_Classroom, Long> {
    List<Account_Classroom> findAccount_ClassroomsByAccountId(Long id);
    List<Account_Classroom> findAccount_ClassroomsByClassroomId(Long id);
    List<Account_Classroom> findAccount_ClassroomsByClassroomIdAndPosition(Long id, String position);

    @Query("SELECT ass FROM Account_Classroom ass WHERE (ass.position = ?1 OR ass.position= ?2) AND ass.accountId = ?3")
    List<Account_Classroom> findAccountClassroomByPosition(String teacher, String student, Long id);


    Optional<Account_Classroom> findAccount_ClassroomByClassroomId(Long id);
    Optional<Account_Classroom> findAccount_ClassroomByAccountId(Long id);
    Optional<Account_Classroom> findAccount_ClassroomByClassroomIdAndAccountId(Long classroomId, Long studentId);

    Optional<Account_Classroom> findAccount_ClassroomByClassroomIdAndPosition(Long id, String position);

    boolean existsAccount_ClassroomByAccountId(Long id);
    boolean existsAccount_ClassroomByClassroomId(Long id);
}
