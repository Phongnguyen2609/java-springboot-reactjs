package com.example.fai_edu_classroom.controller;

import com.example.fai_edu_classroom.command.ChangeStatusCommentCommand;
import com.example.fai_edu_classroom.dto.request.CommentRequest;
import com.example.fai_edu_classroom.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommnetController {
    @Autowired
    CommentService commentService;

    // => Test Ok
    @GetMapping("/get-all-comment-to exercise")
    public ResponseEntity<?> getAllCommentsToExercise(@RequestParam("exercise") Long exerciseId) {
        return commentService.getAllCommentsToExercise(exerciseId);
    }

    // => Test Ok
    @PostMapping("/add-comment-to-exercise")
    public ResponseEntity<?> addCommentsToExercise(@RequestParam("account") Long accountId,
                                                   @RequestParam("classroom") Long classroomId,
                                                   @RequestParam("exercise") Long exerciseId,
                                                   @RequestBody CommentRequest commentRequest) {
        return commentService.addCommentsToExercise(accountId, classroomId, exerciseId, commentRequest);
    }


    @PostMapping("/teacher-hide-comment-to-exercise")
    public ResponseEntity<?> teacherHideCommentsToExercise(@RequestParam("account") Long accountId,
                                                           @RequestParam("classroom") Long classroomId,
                                                           @RequestParam("exercise") Long exerciseId,
                                                           @RequestParam("comment") Long commentId) {
        return commentService.teacherHideCommentsToExercise(accountId, classroomId, exerciseId, commentId);
    }

    @PostMapping("/teacher-show-comment-to-exercise")
    public ResponseEntity<?> teacherShowCommentsToExercise(@RequestParam("account") Long accountId,
                                                           @RequestParam("classroom") Long classroomId,
                                                           @RequestParam("exercise") Long exerciseId,
                                                           @RequestParam("comment") Long commentId) {
        return commentService.teacherShowCommentsToExercise(accountId, classroomId, exerciseId, commentId);
    }

    // => Test Ok
    @DeleteMapping("/student-delete-comment-exercise")
    public ResponseEntity<?> studentDeleteComment(@RequestParam("account") Long accountId,
                                                  @RequestParam("exercise") Long exerciseId,
                                                  @RequestParam("comment") Long commentId) {
        return commentService.studentDeleteCommnet(accountId, exerciseId, commentId);
    }

    @DeleteMapping("/teacher-delete-comment-exercise")
    public ResponseEntity<?> TeacherDeleteComment(@RequestParam("account") Long accountId,
                                                  @RequestParam("classroom") Long classroomId,
                                                  @RequestParam("exercise") Long exerciseId,
                                                  @RequestParam("comment") Long commentId) {
        return commentService.TeacherDeleteCommnet(accountId,classroomId, exerciseId, commentId);
    }

}
