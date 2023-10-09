package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.dto.request.ClassroomRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Account_ClassroomsResponse {
    private List<ClassroomRequest> classrooms;
}
