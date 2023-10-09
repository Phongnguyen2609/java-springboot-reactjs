package com.example.fai_edu_classroom.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account_ExerciseRequest {
    private Long id;
    private Long accountId;
    private Long exerciseId;
    private Double point;

//    public Account_ExerciseRequest(Account_Exercise account_exercise){
//        this.point =
//    }
}
