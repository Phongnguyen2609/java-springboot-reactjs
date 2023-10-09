package com.example.fai_edu_classroom.services;

import com.example.fai_edu_classroom.command.Exercise_StatusCommand;
import com.example.fai_edu_classroom.dto.request.ExerciseRequest;
import com.example.fai_edu_classroom.dto.request.StudentRequest;
import com.example.fai_edu_classroom.dto.response.MessageResponse;
import com.example.fai_edu_classroom.dto.response.Student_ListExerciseResponse;
import com.example.fai_edu_classroom.entity.PostAss;
import com.example.fai_edu_classroom.entity.relationship.Account_Classroom;
import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Account_Post_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Classroom_Exercise;
import com.example.fai_edu_classroom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    IClassroomRepository iClassroomRepository;

    @Autowired
    IClassroom_ExerciseRepository iClassroom_exerciseRepository;

    @Autowired
    IExerciseRepository iExerciseRepository;

    @Autowired
    IAccount_ExerciseRepository iAccount_exerciseRepository;

    @Autowired
    IAccountRepository iAccountRepository;

    @Autowired
    IPostAssRepository iPostAssRepository;

    @Autowired
    IAccount_Post_ExerciseRepository iAccount_postExerciseRepository;

    @Autowired
    IAccount_ClassroomRepository iAccount_classroomRepository;

    /*
        => test OK
     */

    public ResponseEntity<?> accountPostExericse(Long accountId, Long classroomId, Long exerciseId, MultipartFile file) {
        var find_student = iAccountRepository.findById(accountId).get();
        var find_classroom = iClassroomRepository.findById(classroomId).get();
        var find_exercise = iExerciseRepository.findById(exerciseId);
        if (find_exercise.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("exercise not exists"));
        }
        PostAss post = new PostAss();
        // tìm ra bài tập
        var find_classroom_exercises_status = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomIdAndStatus(find_classroom.getId(), "assignment");
        for (Classroom_Exercise classroom_exercise : find_classroom_exercises_status) {
//            var check_classroom_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
            if (classroom_exercise.getExerciseId().equals(find_exercise.get().getId())) {
//                return ResponseEntity.badRequest().body(new MessageResponse("This assignment is not available in the classroom"));
                // lấy ra được danh sách học có bài tập
                var find_account_exercises = iAccount_exerciseRepository.findAccount_ExercisesByAccountId(find_student.getId());
                if (find_account_exercises.size() == 0) {
                    return ResponseEntity.badRequest().body(new MessageResponse("this student not exercise"));
                }

                // tìm ra danh sách account đã post exercise lên check xem nó đã post chưa
                var find_accounts_post_exercise = iAccount_postExerciseRepository.findAccount_Post_ExercisesByExerciseId(exerciseId);
                for (Account_Exercise ae : find_account_exercises) {
                    var check_exercise = iExerciseRepository.findById(ae.getExerciseId()).get();

                    if (check_exercise.getId().equals(exerciseId)) {

                        post.setFile(file.getOriginalFilename());

                        // Parse the string to LocalDate
                        LocalDateTime postNow = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String postGetTime = formatter.format(postNow);

                        post.setTime_post(postGetTime);
                        iPostAssRepository.save(post);

                        Account_Post_Exercise account_post = new Account_Post_Exercise();
                        account_post.setAccountId(find_student.getId());
                        account_post.setPostId(post.getId());
                        account_post.setExerciseId(exerciseId);

                        for (Account_Post_Exercise account_post_exercise : find_accounts_post_exercise) {
                            if (account_post_exercise.getAccountId().equals(find_student.getId())) {
                                return ResponseEntity.badRequest().body(new MessageResponse(find_student.getUsername() + " already post exercise"));
                            }
                        }
                        if (postGetTime.compareTo(classroom_exercise.getDeadline()) > 0) {
                            account_post.setStatus("late");
                        } else {
                            account_post.setStatus("complete");
                        }
                        iAccount_postExerciseRepository.save(account_post);
                        return ResponseEntity.ok().body(new MessageResponse("student post exercise successfully"));
                    }
                }
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("student post exercise faild"));
    }

    public ResponseEntity<?> studentDeleteClassroom(Long studentId, Long classroomId) {
        var find_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (find_classroom.getAccountId().equals(studentId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("The teacher will not be allowed to leave the classroom"));
        }
        var find_students = iAccount_classroomRepository.findAccount_ClassroomsByClassroomIdAndPosition(classroomId, "student");
        var check_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndAccountId(classroomId, studentId).get();
        for (Account_Classroom account_classroom : find_students) {

            if (account_classroom.getAccountId().equals(studentId)) {
                iAccount_classroomRepository.deleteById(check_classroom.getId());
                return ResponseEntity.ok().body(new MessageResponse("successfully leave the classroom"));
            }

        }
        return ResponseEntity.badRequest().body(new MessageResponse("Can't leave the classroom"));
    }

    public ResponseEntity<?> accountUpdatePostExericse(Long accountId, Long classroomId, Long exerciseId, MultipartFile file) {
//        var find_student = iAccountRepository.findById(accountId)
        return null;
    }

    public ResponseEntity<?> getStudentListExercise(Long accountId, Long classroomId) {
        var find_account = iAccountRepository.findById(accountId).get();
        var find_account_classrooms = iAccount_classroomRepository.findAccount_ClassroomsByAccountId(find_account.getId());
        Student_ListExerciseResponse student_listExerciseResponse = new Student_ListExerciseResponse();
        for (Account_Classroom account_classroom : find_account_classrooms) {
            if (account_classroom.getClassroomId().equals(classroomId)) {
                var find_classroom_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomIdAndStatus(classroomId, "assignment");

                StudentRequest studentRequest = new StudentRequest(find_account);
                student_listExerciseResponse.setStudent(studentRequest);

                student_listExerciseResponse.setStudent(studentRequest);

                List<Exercise_StatusCommand> exercise_statusCommands = new ArrayList<>();
                for (Classroom_Exercise classroom_exercise : find_classroom_exercises) {
                    var check_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
                    // lay ra duoc Student_StatusCommand
                    Exercise_StatusCommand exercise_statusCommand = new Exercise_StatusCommand();
                    exercise_statusCommand.setExerciseId(check_exercise.getId());
                    exercise_statusCommand.setExercise_name(check_exercise.getTitle());
                    var find_exercises = iAccount_exerciseRepository.findAccount_ExercisesByAccountId(accountId);
                    for (Account_Exercise account_exercise : find_exercises) {
                        // Nếu classroom có Exercise và Account cũng có Exercise ý
                        if (account_exercise.getExerciseId().equals(classroom_exercise.getExerciseId())) {
                            if (account_exercise.getPoint() == null) {
                                exercise_statusCommand.setPoint(0.0);
                            } else {
                                exercise_statusCommand.setPoint(account_exercise.getPoint());
                            }
                            var find_status = iAccount_postExerciseRepository.findAccount_Post_ExercisesByAccountId(account_exercise.getAccountId());
                            for (Account_Post_Exercise account_post_exercise : find_status) {

                                if (account_exercise.getExerciseId().equals(account_post_exercise.getExerciseId())) {
                                    exercise_statusCommand.setStatus(account_post_exercise.getStatus());
                                } else {
                                    exercise_statusCommand.setStatus("Haven't sent exercise");
                                }
                            }

                            exercise_statusCommand.setDeadline(classroom_exercise.getDeadline());

                            exercise_statusCommands.add(exercise_statusCommand);
                        }
                    }
                }
                student_listExerciseResponse.setExercises(exercise_statusCommands);
                return ResponseEntity.ok().body(student_listExerciseResponse);
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("account not classroom"));
    }
}
