package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.entity.Account;
import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Account_Post_Exercise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Student_Point_ExerciseResponse {
    private Long studentId;
    private String username;
    private Double point;

    public Student_Point_ExerciseResponse (Account account, Account_Exercise account_exercise){
        this.studentId = account.getId();
        this.username = account.getUsername();
        this.point = account_exercise.getPoint();
    }
}
