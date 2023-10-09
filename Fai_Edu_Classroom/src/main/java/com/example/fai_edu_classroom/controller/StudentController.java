package com.example.fai_edu_classroom.controller;

import com.example.fai_edu_classroom.services.StudentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping(value ="/account-post-exercise", headers = "Content-Type=multipart/form-data")
    public ResponseEntity<?> accountPostExericse(@RequestParam("account") Long accountId,
                                                 @RequestParam("classroom") Long classroomId,
                                                 @RequestParam("exercise") Long exerciseId,
                                                 @RequestParam(value = "file", required = true) MultipartFile file){
        return studentService.accountPostExericse(accountId, classroomId, exerciseId, file);
    }

    @PostMapping(value ="/account-update-post-exercise", headers = "Content-Type=multipart/form-data")
    public ResponseEntity<?> accountUpdatePostExericse(@RequestParam("account") Long accountId,
                                                 @RequestParam("classroom") Long classroomId,
                                                 @RequestParam("exercise") Long exerciseId,
                                                 @RequestParam(value = "file", required = true) MultipartFile file){
        return studentService.accountUpdatePostExericse(accountId, classroomId, exerciseId, file);
    }

    // Học sinh rời khỏi lớp
    @DeleteMapping("/delete-classroom")
    public ResponseEntity<?> studentDeleteClassroom(@RequestParam("student") Long studentId,
                                                    @RequestParam(name = "classroom") Long classroomId) {
        return studentService.studentDeleteClassroom(studentId, classroomId);
    }

    @GetMapping("/student-list-exercise")
    public ResponseEntity<?> getStudentListExercise(@RequestParam("account") Long accountId,
                                                    @RequestParam("classroom") Long classroomId
                                                    ){
        return studentService.getStudentListExercise(accountId, classroomId);
    }
}
