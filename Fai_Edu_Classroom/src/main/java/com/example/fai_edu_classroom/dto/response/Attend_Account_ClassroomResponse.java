package com.example.fai_edu_classroom.dto.response;

import com.example.fai_edu_classroom.dto.request.StudentRequest;
import com.example.fai_edu_classroom.dto.request.TeacherRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Attend_Account_ClassroomResponse {
    private Long classroomId;
    private String classroomName;
    private TeacherRequest teacher;
    private int number;
    private List<StudentRequest> students;
}
