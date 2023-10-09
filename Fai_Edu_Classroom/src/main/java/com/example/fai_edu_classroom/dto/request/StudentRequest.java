package com.example.fai_edu_classroom.dto.request;

import com.example.fai_edu_classroom.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StudentRequest {
    private Long studentId;
    private String username;

    public StudentRequest(Account account) {
        this.studentId = account.getId();
        this.username = account.getUsername();
    }
}
