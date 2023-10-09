package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICommentRepository extends JpaRepository<Comment, Long> {

}
