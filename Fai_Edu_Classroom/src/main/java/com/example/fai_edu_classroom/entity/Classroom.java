package com.example.fai_edu_classroom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "classrooms")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "class_name")
    @NotBlank(message = "classroom name not blank")
    private String classroomName;

    @Column(name = "class_code")
    private String code;

    @Column(name = "topic")
    private String topic;

    @Column(name = "room")
    private String room;
}
