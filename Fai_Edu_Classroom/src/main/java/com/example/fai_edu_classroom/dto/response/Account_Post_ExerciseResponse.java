package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.dto.request.Account_Post_ExerciseRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Account_Post_ExerciseResponse {
    private Long exerciseId;
    private String title;
    private int total_students;
    private int number_of_students_submit;
    private int number_of_students_not_submit;
    private List<Account_Post_ExerciseRequest> account_post_exercises;
}
