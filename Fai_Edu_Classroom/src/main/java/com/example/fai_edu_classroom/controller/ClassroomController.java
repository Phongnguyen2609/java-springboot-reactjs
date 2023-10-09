package com.example.fai_edu_classroom.controller;

import com.example.fai_edu_classroom.command.AddStudentCommand;
import com.example.fai_edu_classroom.dto.request.ClassroomRequest;
import com.example.fai_edu_classroom.dto.request.RegisterRequest;
import com.example.fai_edu_classroom.dto.request.StudentRequest;
import com.example.fai_edu_classroom.dto.response.StudentResponse;
import com.example.fai_edu_classroom.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classroom")
public class ClassroomController {
    @Autowired
    ClassroomService classroomService;

    @GetMapping("/get-classroom")
    public ResponseEntity<?> getClassroomById(@RequestParam("classroom") Long id) {
        return classroomService.getClassroomById(id);
    }

    // tạo 1 lớp học
    @PostMapping("/create-classroom")
    public ResponseEntity<?> createClassroom(@Validated @RequestParam("account") Long account,
                                             @RequestBody ClassroomRequest classroomRequest) {
        return classroomService.createClassroom(account, classroomRequest);
    }

    @PostMapping("/confirm-attend-classroom")
    public ResponseEntity<?> confirmAttendClassroomByCode(@RequestParam("account") Long accountId,
                                                          @RequestParam("classroom") Long classroomId,
                                                          @RequestBody AddStudentCommand addStudentCommand) {
        return classroomService.confirmAttendClassroomByCode(accountId, classroomId, addStudentCommand);
    }

    @GetMapping("/get-attend-classroom-awaiting")
    public ResponseEntity<?> getAttendClassroomByAwaiting(@RequestParam("account") Long accountId,
                                                          @RequestParam("classroom") Long classroomId) {
        return classroomService.getAttendClassroomByAwaiting(accountId, classroomId);
    }

    @PostMapping("/attend-classroom")
    public ResponseEntity<?> attendClassroomByCode(@RequestParam("account") Long account,
                                                   @RequestBody ClassroomRequest classroomRequest) {
        return classroomService.attendClassroomByCode(account, classroomRequest);
    }

    @PostMapping("/add-student-classroom")
    public ResponseEntity<?> addStudentToClassroom(@RequestParam("account") Long accountId,
                                                   @RequestParam("classroom") Long classroomId,
                                                   @RequestBody StudentRequest studentRequest) {
        return classroomService.addStudentToClassroom(accountId, classroomId, studentRequest);
    }

    @GetMapping("/search-student")
    public ResponseEntity<?> searchStudent(@RequestParam(name = "query") String query) {
        return classroomService.searchStudent(query);
    }

    @GetMapping("/get-students-classroom")
    public ResponseEntity<?> getStudentsFromClassroom(@RequestParam("classroom") Long id) {
        return classroomService.getStudentsFromClassroom(id);
    }

    @DeleteMapping("/delete-student-from-classroom")
    public ResponseEntity<?> deleteStudentFromClassroom(@RequestParam("account") Long accountId,
                                                        @RequestParam("classroom") Long classroomId,
                                                        @RequestParam("student") Long studentId) {
        return classroomService.deleteStudentFromClassroom(accountId, classroomId, studentId);
    }

    // tạo 1 lớp học
    // @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/update-classroom")
    public ResponseEntity<?> updateClassroom(@Validated @RequestParam(name = "classroomId") Long id) {
        return classroomService.updateClassroom(id);
    }


    // lây ra danh sách bài tập theo class
    @GetMapping("/get-exercise-to-classroom")
    public ResponseEntity<?> getExecisesToClassroom(@RequestParam("classroom") Long id) {
        return classroomService.getExecisesToClassroom(id);
    }

    @GetMapping("/get-classroom-to-account")
    public ResponseEntity<?> getClassroomsToAccount(@RequestParam("account") Long id) {
        return classroomService.getClassroomsToAccount(id);
    }

    // chi tiết bài tập
    @GetMapping("/get-detai-exercise-to-classroom")
    public ResponseEntity<?> getDetailExerciseToClassroom(@RequestParam("classroom") Long classroomId,
                                                          @RequestParam("classroom") Long exerciseId) {
        return classroomService.getDetailExerciseToClassroom(classroomId, exerciseId);
    }

}
