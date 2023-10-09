package com.example.fai_edu_classroom.services;

import com.example.fai_edu_classroom.dto.response.MessageResponse;
import com.example.fai_edu_classroom.entity.Classroom;
import com.example.fai_edu_classroom.entity.Exercise;
import com.example.fai_edu_classroom.entity.relationship.Account_Classroom;
import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Classroom_Exercise;
import com.example.fai_edu_classroom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Service
public class ExerciseService {
    @Autowired
    IExerciseRepository iExerciseRepository;

    @Autowired
    IAccount_ExerciseRepository iAccount_exerciseRepository;

    @Autowired
    IClassroom_ExerciseRepository iClassroom_exerciseRepository;

    @Autowired
    IClassroomRepository iClassroomRepository;

    @Autowired
    IAccount_ClassroomRepository iAccount_classroomRepository;

    /*
        => Test OK
     */
    public ResponseEntity<?> createExriciseDocument(Long accountId, Long classroomId, String title, String description, MultipartFile file) {
        try {
            var find_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
            if (!accountId.equals(find_teacher.getAccountId())){
                return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
            }
            var find_classroom = iClassroomRepository.findById(classroomId);

            Classroom classroom = find_classroom.get();
            Exercise exercise = new Exercise();
            exercise.setTitle(title);
            exercise.setDescription(description);
            exercise.setFile(file.getOriginalFilename());
            exercise.setType("document");
            iExerciseRepository.save(exercise);

            Classroom_Exercise classroom_exercise = new Classroom_Exercise();
            classroom_exercise.setClassroomId(classroom.getId());
            classroom_exercise.setExerciseId(exercise.getId());
            classroom_exercise.setStatus("document");
            iClassroom_exerciseRepository.save(classroom_exercise);

            return ResponseEntity.ok().body(new MessageResponse("upload exercise document successully"));

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("upload exercise document faild"));
        }

    }

    /*
        - truyền vào id lớp học
        => lấy ra được danh sách học sinh đang học tại lớp ý
        - add bài tập cho tất cả học sinh đang học tại lớp ý
        => Test OK
     */

    public ResponseEntity<?> createExriciseAttachment(Long accountId, Long classroomId, String title, String description, double point, String deadline, MultipartFile file) {
        try {
            var find_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
            if (!accountId.equals(find_teacher.getAccountId())){
                return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
            }
            var find_classroom = iClassroomRepository.findById(classroomId);

            Classroom classroom = find_classroom.get();
            Exercise exercise = new Exercise();
            exercise.setTitle(title);
            exercise.setDescription(description);
            exercise.setFile(file.getOriginalFilename());
            exercise.setType("exercise");
            iExerciseRepository.save(exercise);

            Classroom_Exercise classroom_exercise = new Classroom_Exercise();
            classroom_exercise.setClassroomId(classroom.getId());
            classroom_exercise.setExerciseId(exercise.getId());
            classroom_exercise.setPoint(point);
            try {
                // Define the date format using DateTimeFormatter
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDateTime postNow = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String postGetTime = formatter.format(postNow);

                classroom_exercise.setDeadline(deadline);

//                post.setTime_post(postGetTime);
//
//                // Parse the string to LocalDate
//                LocalDate localDate = LocalDate.parse(deadline, formatter);
//                LocalDate localDate1 = LocalDate.now();
                if (postGetTime.compareTo(classroom_exercise.getDeadline()) > 0){
                    return ResponseEntity.badRequest().body(new MessageResponse("deadline cannot be less than current date"));
                }
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
            classroom_exercise.setStatus("assignment");
            iClassroom_exerciseRepository.save(classroom_exercise);

            var find_classroom_accounts = iAccount_classroomRepository.findAccount_ClassroomsByClassroomIdAndPosition(classroomId, "student");
            for (Account_Classroom account_classroom : find_classroom_accounts){

                Account_Exercise account_exercise = new Account_Exercise();
                account_exercise.setAccountId(account_classroom.getAccountId());
                account_exercise.setExerciseId(exercise.getId());
                iAccount_exerciseRepository.save(account_exercise);
            }
            return ResponseEntity.ok().body(new MessageResponse("upload exercise successully"));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("upload exercise faild"));
        }
    }
}
