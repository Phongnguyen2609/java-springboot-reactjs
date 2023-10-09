package com.example.fai_edu_classroom.dto.request;

import com.example.fai_edu_classroom.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TeacherRequest {
    private Long teacherId;
    private String username;

    public TeacherRequest(Account account) {
        this.teacherId = account.getId();
        this.username = account.getUsername();
    }
}
