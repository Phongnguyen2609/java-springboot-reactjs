package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.dto.request.ExerciseRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Classroom_ExerciseResponse {
    private Long classroomId;
    private String classroomName;
    private List<ExerciseRequest> exercises;

}
