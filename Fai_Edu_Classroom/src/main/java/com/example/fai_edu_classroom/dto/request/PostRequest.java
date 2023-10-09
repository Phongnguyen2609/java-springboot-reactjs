package com.example.fai_edu_classroom.dto.request;

import com.example.fai_edu_classroom.entity.PostAss;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class PostRequest {
    private Long id;

    private String file;

    private LocalDate time_post;

    public PostRequest(PostAss postAss){
        this.file = postAss.getFile();
    }
}
