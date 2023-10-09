package com.example.fai_edu_classroom.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Exercises_StudentsResponse {
    List<Exercise_Students_PointResponse> list_exercises_students;
}
