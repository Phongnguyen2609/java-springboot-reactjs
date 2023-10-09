package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.dto.request.CommentRequest;
import com.example.fai_edu_classroom.dto.request.StudentRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class Account_CommentResponse {
    private StudentRequest student;
    private CommentRequest comment;
    private LocalDate comment_date;
}
