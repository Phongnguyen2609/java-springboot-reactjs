package com.example.fai_edu_classroom.dto.request;

import com.example.fai_edu_classroom.entity.Account;
import com.example.fai_edu_classroom.entity.PostAss;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Account_Post_ExerciseRequest {
    private Long accountId;
    private String username;
    private Long postId;
    private String file_name;
    private String time_post;

    public Account_Post_ExerciseRequest(Account account, PostAss postAss){
        this.accountId = account.getId();
        this.username = account.getUsername();

        this.postId = postAss.getId();
        this.file_name = postAss.getFile();
        this.time_post = postAss.getTime_post();
    }

}
