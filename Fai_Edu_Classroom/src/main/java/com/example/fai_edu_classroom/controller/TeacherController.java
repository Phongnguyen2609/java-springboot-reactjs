package com.example.fai_edu_classroom.controller;

import com.example.fai_edu_classroom.dto.request.Account_ExerciseRequest;
import com.example.fai_edu_classroom.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @PostMapping("/update-point")
    public ResponseEntity<?> updatePoint(@RequestParam("account") Long accountId,
                                         @RequestParam("classroom") Long classroomId,
                                         @RequestParam("student") Long studentId,
                                         @RequestParam("exercise") Long exerciseId,
                                         @RequestBody Account_ExerciseRequest account_exerciseRequest) {
        return teacherService.updatePoint(accountId, classroomId, exerciseId, studentId, account_exerciseRequest);
    }

    @GetMapping("/get-all-account-post-exercise")
    public ResponseEntity<?> getAllAccountsPostExercise(@RequestParam("account") Long accountId,
                                                        @RequestParam("classroom") Long classroomId,
                                                        @RequestParam("exercise") Long exerciseId) {
        return teacherService.getAllAccountsPostExercise(accountId, classroomId, exerciseId);
    }

    // lấy chi tiết student- post- exercise
    @GetMapping("/get-account-post-exercise")
    public ResponseEntity<?> getAccountsPostExercise(@RequestParam("account") Long accountId,
                                                     @RequestParam("classroom") Long classroomId,
                                                     @RequestParam("exercise") Long exerciseId,
                                                     @RequestParam("student") Long studentId){
        return teacherService.getAccountsPostExercise(accountId, classroomId, exerciseId, studentId);
    }

    @DeleteMapping("/delete-exercise")
    public ResponseEntity<?> deleteExercise(@RequestParam("account") Long accountId,
                                            @RequestParam("classroom") Long classroomId,
                                            @RequestParam("exercise") Long exerciseId){
        return teacherService.deleteExercise(accountId, classroomId, exerciseId);
    }

    @GetMapping("/get-all-exercise-students")
    public ResponseEntity<?> getAllExercisesStudents(@RequestParam("account") Long accountId,
                                                     @RequestParam("classroom") Long classroomId){
        return teacherService.getAllExercisesStudents(accountId, classroomId);
    }
}
