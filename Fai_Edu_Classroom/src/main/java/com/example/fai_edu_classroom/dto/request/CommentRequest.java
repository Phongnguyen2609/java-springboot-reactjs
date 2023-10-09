package com.example.fai_edu_classroom.dto.request;

import com.example.fai_edu_classroom.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class CommentRequest {
    private Long commentId;
    private String content;

    public CommentRequest(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getConten();
    }
}
