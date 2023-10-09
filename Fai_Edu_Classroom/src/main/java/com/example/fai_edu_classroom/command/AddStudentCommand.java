package com.example.fai_edu_classroom.command;

import com.example.fai_edu_classroom.dto.request.StudentRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddStudentCommand {
    private List<StudentRequest> students;
}
