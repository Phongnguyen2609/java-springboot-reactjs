package com.example.fai_edu_classroom.entity.relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "accounts_comment_exercises")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account_Comment_Exercise {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "comment")
    private Long commentId;

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Column(name = "status")
    private String status;

    @Column(name = "comment_date")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate commnet_date;
}
