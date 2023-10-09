package com.example.fai_edu_classroom.services;

import com.example.fai_edu_classroom.dto.request.Account_ExerciseRequest;
import com.example.fai_edu_classroom.dto.request.Account_Post_ExerciseRequest;
import com.example.fai_edu_classroom.dto.response.*;
import com.example.fai_edu_classroom.entity.Account;
import com.example.fai_edu_classroom.entity.relationship.Account_Classroom;
import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Account_Post_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Classroom_Exercise;
import com.example.fai_edu_classroom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {
    @Autowired
    IAccountRepository iAccountRepository;

    @Autowired
    IAccount_ClassroomRepository iAccount_classroomRepository;

    @Autowired
    IAccount_ExerciseRepository iAccount_exerciseRepository;

    @Autowired
    IClassroom_ExerciseRepository iClassroom_exerciseRepository;

    @Autowired
    IExerciseRepository iExerciseRepository;

    @Autowired
    IAccount_Post_ExerciseRepository iAccount_postExerciseRepository;

    @Autowired
    IPostAssRepository iPostAssRepository;

    /*
        - accountId => lấy ra id đăng nhập
        - classroomId là click vào lớp đó => lấy ra id của lớp đó
        - hiển thị ra danh sách bài tập
     */
    public ResponseEntity<?> updatePoint(Long accountId, Long classroomId, Long exerciseId, Long studentId, Account_ExerciseRequest account_exerciseRequest) {
        var find_student = iAccountRepository.findById(studentId).get();
        // lấy ra danh sách có account là teacher trong id lớp
        var find_account_teacher_in_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        // check xem id có phải teacher hay không
        if (!find_account_teacher_in_classroom.getAccountId().equals(accountId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }

        // lấy ra danh sách học sinh trong lớp
        var find_account_students_classroom = iAccount_classroomRepository.findAccount_ClassroomsByClassroomIdAndPosition(classroomId, "student");

        // lấy ra danh sách bài tập theo id classroom
        var find_classroom_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomIdAndStatus(classroomId, "assignment");
        for (Classroom_Exercise classroom_exercise : find_classroom_exercises) {
            var find_exercise_accounts = iAccount_exerciseRepository.findAccount_ExercisesByExerciseId(exerciseId);
            // lấy từ bảng bài tập có học sinh
            for (Account_Exercise account_exercise : find_exercise_accounts) {
                // lấy từ bảng account post exercise
                var find_account_post_exercises = iAccount_postExerciseRepository.findAccount_Post_ExercisesByExerciseId(exerciseId);
                for (Account_Post_Exercise account_post_exercise : find_account_post_exercises) {
                    if (account_exercise.getAccountId().equals(studentId) && account_post_exercise.getAccountId().equals(studentId)) {

                        account_exercise.setAccountId(account_exercise.getAccountId());
                        account_exercise.setExerciseId(account_exercise.getExerciseId());
                        account_exercise.setPoint(account_exerciseRequest.getPoint());

                        if (classroom_exercise.getPoint() < account_exercise.getPoint()) {
                            return ResponseEntity.badRequest().body(new MessageResponse("the score cannot be greater than the maximum score"));
                        }
                        iAccount_exerciseRepository.save(account_exercise);
                        return ResponseEntity.ok().body(new MessageResponse("update point successfully"));
                    }

                }
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse(find_student.getUsername() + " Haven't uploaded the exercise yet"));
    }

    public ResponseEntity<?> getAllAccountsPostExercise(Long accountId, Long classroomId, Long exerciseId) {
        var find_account = iAccountRepository.findById(accountId);

        var find_exercise = iExerciseRepository.findById(exerciseId).get();
        // lấy ra danh sách có account là teacher trong id lớp
        var find_account_teacher_in_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        // check xem id có phải teacher hay không
        if (!find_account_teacher_in_classroom.getAccountId().equals(find_account.get().getId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("This account can't see the exercise list"));
        }

        // lấy danh sách account đã nộp bài tập
        var find_accounts_post_exercise = iAccount_postExerciseRepository.findAccount_Post_ExercisesByExerciseId(exerciseId);
        var find_accounts_exercises = iAccount_exerciseRepository.findAccount_ExercisesByExerciseId(find_exercise.getId());

        Account_Post_ExerciseResponse account_post_exerciseResponse = new Account_Post_ExerciseResponse();
        account_post_exerciseResponse.setExerciseId(find_exercise.getId());
        account_post_exerciseResponse.setTitle(find_exercise.getTitle());

        account_post_exerciseResponse.setTotal_students(find_accounts_exercises.size());
        account_post_exerciseResponse.setNumber_of_students_submit(find_accounts_post_exercise.size());
        account_post_exerciseResponse.setNumber_of_students_not_submit(find_accounts_exercises.size() - account_post_exerciseResponse.getNumber_of_students_submit());

        List<Account_Post_ExerciseRequest> account_post_exerciseRequests = new ArrayList<>();
        for (Account_Post_Exercise account_post_exercise : find_accounts_post_exercise) {
            var check_account = iAccountRepository.findById(account_post_exercise.getAccountId()).get();
            var check_post = iPostAssRepository.findById(account_post_exercise.getPostId()).get();
            Account_Post_ExerciseRequest account_post_exerciseRequest = new Account_Post_ExerciseRequest(check_account, check_post);
            account_post_exerciseRequests.add(account_post_exerciseRequest);
        }
        account_post_exerciseResponse.setAccount_post_exercises(account_post_exerciseRequests);
        return ResponseEntity.ok().body(account_post_exerciseResponse);
    }


    public ResponseEntity<?> getAccountsPostExercise(Long accountId, Long classroomId, Long exerciseId, Long studentId) {
        var find_exercise = iExerciseRepository.findById(exerciseId).get();
        // lấy ra danh sách có account là teacher trong id lớp
        var find_account_teacher_in_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        // check xem id có phải teacher hay không
        if (!find_account_teacher_in_classroom.getAccountId().equals(accountId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("This account can't see the exercise list"));
        }

        // tìm ra danh sách học sinh đã post exercise

        var find_students_post_exercise = iAccount_postExerciseRepository.findAccount_Post_ExercisesByExerciseId(exerciseId);
        for (Account_Post_Exercise account_post_exercise : find_students_post_exercise){
            if (account_post_exercise.getAccountId().equals(studentId)){
                var check_account = iAccountRepository.findById(account_post_exercise.getAccountId()).get();
                var check_post = iPostAssRepository.findById(account_post_exercise.getPostId()).get();
                Account_Post_ExerciseRequest account_post_exerciseRequest = new Account_Post_ExerciseRequest(check_account, check_post);
                return ResponseEntity.ok().body(account_post_exerciseRequest);
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error"));
    }

    public ResponseEntity<?> deleteExercise(Long accountId, Long classroomId, Long exerciseId) {
        var find_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (!find_teacher.getAccountId().equals(accountId)){
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }

        var find_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomId(classroomId);
        for (Classroom_Exercise classroom_exercise : find_exercises){
            var check_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
            if (check_exercise.getId().equals(exerciseId)){
                iExerciseRepository.deleteById(check_exercise.getId());

                var check_classroom_exericse = iClassroom_exerciseRepository.findClassroom_ExerciseByExerciseId(check_exercise.getId()).get();
                iClassroom_exerciseRepository.deleteById(check_classroom_exericse.getId());

                return ResponseEntity.ok().body(new MessageResponse("delete exercise successfully"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("delete exercise faild"));
    }

    @Autowired
    IClassroomRepository iClassroomRepository;
    public ResponseEntity<?> getAllExercisesStudents(Long accountId, Long classroomId) {
        var find_classroom = iClassroomRepository.findById(classroomId);
        if (find_classroom.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("classroom not found"));
        }

        var find_account = iAccountRepository.findById(accountId);
        if (find_account.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("account not found"));
        }

        var find_account_teacher_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(find_classroom.get().getId(), "teacher");
        if (!find_account_teacher_classroom.get().getAccountId().equals(find_account.get().getId())){
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }

        List<Exercises_StudentsResponse> exercises_studentsResponses = new ArrayList<>();
        var find_classroom_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomIdAndStatus(find_classroom.get().getId(), "assignment");

        List<Exercise_Students_PointResponse> exercise_students_pointResponses = new ArrayList<>();
        Exercises_StudentsResponse exercises_studentsResponse = new Exercises_StudentsResponse();

        for (Classroom_Exercise classroom_exercise : find_classroom_exercises){
            var check_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
            Exercise_Students_PointResponse exercise_students_pointResponse = new Exercise_Students_PointResponse();
            exercise_students_pointResponse.setExerciseId(check_exercise.getId());
            exercise_students_pointResponse.setTitle(check_exercise.getTitle());

            List<Student_Point_ExerciseResponse> student_point_exerciseResponses = new ArrayList<>();

            var find_exercise_students = iAccount_exerciseRepository.findAccount_ExercisesByExerciseId(check_exercise.getId());
            for (Account_Exercise account_exercise : find_exercise_students){
                var check_account = iAccountRepository.findById(account_exercise.getAccountId()).get();
                Student_Point_ExerciseResponse student_point_exerciseResponse = new Student_Point_ExerciseResponse();
                student_point_exerciseResponse.setStudentId(check_account.getId());
                student_point_exerciseResponse.setUsername(check_account.getUsername());
                var find_point = iAccount_exerciseRepository.findAccount_ExerciseByAccountIdAndExerciseId(check_account.getId(), check_exercise.getId());
                if (find_point.get().getPoint()== null){
                    student_point_exerciseResponse.setPoint(0.0);
                } else {
                    student_point_exerciseResponse.setPoint(find_point.get().getPoint());
                }
                // Đây mới ra được student point exercise
                student_point_exerciseResponses.add(student_point_exerciseResponse);
            }
            // đây là thêm vào exercise students
            exercise_students_pointResponse.setStudent_points(student_point_exerciseResponses);
            exercise_students_pointResponses.add(exercise_students_pointResponse);
        }
        exercises_studentsResponse.setList_exercises_students(exercise_students_pointResponses);
        return ResponseEntity.ok().body(exercises_studentsResponse);
    }

}
