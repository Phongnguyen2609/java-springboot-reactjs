package com.example.fai_edu_classroom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "exercises")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "title")
    @NotBlank(message = "title not blank")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "file")
    private String file;

    @Column(name = "type")
    private String type;
}
