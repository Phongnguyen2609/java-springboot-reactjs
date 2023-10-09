package com.example.fai_edu_classroom.command;

import com.example.fai_edu_classroom.dto.request.ExerciseRequest;
import com.example.fai_edu_classroom.entity.Classroom;
import com.example.fai_edu_classroom.entity.Exercise;
import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Account_Post_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Classroom_Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class Exercise_StatusCommand {
    private Long exerciseId;
    private String exercise_name;
    // lay tu bang account_post_exercise
    private String status;
    // lay tu bang account_exercise
    private double point;
    private String deadline;

    public Exercise_StatusCommand (Exercise exercise, Classroom_Exercise classroom_exercise, Account_Post_Exercise account_post_exercise, Account_Exercise account_exercise){
        this.exerciseId = exercise.getId();
        this.exercise_name = exercise.getTitle();
        this.status = account_post_exercise.getStatus();
        this.point = account_exercise.getPoint();
        this.deadline = classroom_exercise.getDeadline();
    }
}
