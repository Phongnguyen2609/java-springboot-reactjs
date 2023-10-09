package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.dto.request.StudentRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StudentAwaitingResponse {
    private int number_awaiting;
    private List<StudentRequest> students;

}
