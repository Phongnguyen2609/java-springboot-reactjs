package com.example.fai_edu_classroom.entity.relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "accounts_post_exercises")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account_Post_Exercise {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Column(name = "status")
    private String status;
}
