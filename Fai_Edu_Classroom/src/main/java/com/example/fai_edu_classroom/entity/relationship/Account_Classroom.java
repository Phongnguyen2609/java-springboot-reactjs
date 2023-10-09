package com.example.fai_edu_classroom.entity.relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "accounts_classrooms")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account_Classroom {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "classroom_id")
    private Long classroomId;

    @Column(name = "position")
    private String position;

}
