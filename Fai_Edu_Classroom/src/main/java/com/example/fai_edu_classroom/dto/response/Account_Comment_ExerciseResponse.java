package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.dto.request.CommentRequest;
import com.example.fai_edu_classroom.dto.request.StudentRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Account_Comment_ExerciseResponse {
    private int number_comment;
    private List<Account_CommentResponse> account_comments;
}
