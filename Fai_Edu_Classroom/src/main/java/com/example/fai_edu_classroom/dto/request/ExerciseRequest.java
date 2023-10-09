package com.example.fai_edu_classroom.dto.request;

import com.example.fai_edu_classroom.entity.Exercise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ExerciseRequest {
    private Long exerciseId;

    private String title;

    private String description;

    private String file;
    private String type;

    public ExerciseRequest(Exercise exercise){
        this.exerciseId = exercise.getId();
        this.title = exercise.getTitle();
        this.description = exercise.getDescription();
        this.file = exercise.getFile();
        this.type = exercise.getType();
    }
}
