package com.example.fai_edu_classroom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "accounts", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_name")
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Size(min = 5, max = 255)
    @Column(name = "user_name")
    private String username;

    @Size(min = 6, max = 255)
    @Column(name = "password")
    private String password;
}
