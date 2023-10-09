package com.example.fai_edu_classroom.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Exercise_Students_PointResponse {
    private Long exerciseId;
    private String title;

    private List<Student_Point_ExerciseResponse> student_points;
}
