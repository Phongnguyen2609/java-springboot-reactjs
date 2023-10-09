package com.example.fai_edu_classroom.entity.relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "accounts_exercises")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account_Exercise {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Column(name = "point")
    private Double point;
}
