package com.example.fai_edu_classroom.controller;

import com.example.fai_edu_classroom.dto.request.ClassroomRequest;
import com.example.fai_edu_classroom.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    @Autowired
    ExerciseService exerceseService;
//            this.title = exercise.getTitle();
//        this.description = exercise.getDescription();
//        this.file = exercise.getFile();
//        this.point = exercise.getPoint();
//        this.deadline = exercise.getDeadline();


//    @PostMapping("/create-classroom")
//    public ResponseEntity<?> createExercise(@Validated @RequestParam("classroomId") Long classroomId, @RequestBody ClassroomRequest classroomRequest){
//        return exerceseService.createExercise(classroomId, classroomRequest);
//    }

    @PostMapping(value = "/create-exercise-document", headers = "Content-Type=multipart/form-data")
    public ResponseEntity<?> createExerciseDocument(@RequestParam(value = "accountId") Long accountId,
                                                    @RequestParam(value = "classroom") Long classroomId,
                                                    @RequestParam(value = "title", required = true) String title,
                                                    @RequestParam(value = "description", required = false) String description,
                                                    @RequestParam(value = "file", required = true) MultipartFile file) {
        return exerceseService.createExriciseDocument(accountId, classroomId, title, description, file);
    }

    @PostMapping(value = "/create-exercise-attachment", headers = "Content-Type=multipart/form-data")
    public ResponseEntity<?> createExerciseAttachment(@RequestParam(value = "accountId") Long accountId,
                                                      @RequestParam(value = "classroom") Long classroomId,
                                                      @RequestParam(value = "title", required = true) String title,
                                                      @RequestParam(value = "description", required = false) String description,
                                                      @RequestParam(value = "point", required = false) double point,
                                                      @RequestParam(value = "deadline", required = false) String deadline,
                                                      @RequestParam(value = "file", required = false) MultipartFile file) {
        return exerceseService.createExriciseAttachment(accountId, classroomId, title, description, point, deadline, file);

    }


}
