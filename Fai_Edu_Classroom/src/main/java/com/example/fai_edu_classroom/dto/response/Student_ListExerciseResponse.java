package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.command.Exercise_StatusCommand;
import com.example.fai_edu_classroom.dto.request.ExerciseRequest;
import com.example.fai_edu_classroom.dto.request.StudentRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Student_ListExerciseResponse {
    private StudentRequest student;
    private List<Exercise_StatusCommand> exercises;
}
