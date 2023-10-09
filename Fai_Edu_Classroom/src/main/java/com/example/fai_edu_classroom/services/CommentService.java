package com.example.fai_edu_classroom.services;

import com.example.fai_edu_classroom.command.ChangeStatusCommentCommand;
import com.example.fai_edu_classroom.dto.request.CommentRequest;
import com.example.fai_edu_classroom.dto.request.StudentRequest;
import com.example.fai_edu_classroom.dto.response.Account_CommentResponse;
import com.example.fai_edu_classroom.dto.response.Account_Comment_ExerciseResponse;
import com.example.fai_edu_classroom.dto.response.MessageResponse;
import com.example.fai_edu_classroom.entity.Comment;
import com.example.fai_edu_classroom.entity.relationship.Account_Classroom;
import com.example.fai_edu_classroom.entity.relationship.Account_Comment_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Classroom_Exercise;
import com.example.fai_edu_classroom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    ICommentRepository iCommentRepository;

    @Autowired
    IAccount_Comment_ExerciseRepository iAccount_comment_exerciseRepository;

    @Autowired
    IAccount_ExerciseRepository iAccount_exerciseRepository;

    @Autowired
    IAccountRepository iAccountRepository;

    @Autowired
    IAccount_ClassroomRepository iAccount_classroomRepository;

    @Autowired
    IClassroom_ExerciseRepository iClassroom_exerciseRepository;

    @Autowired
    IExerciseRepository iExerciseRepository;


    /*
        Tạo commnet về bài tập cho tất cả mọi người
        - đối tượng được comment là phải học sinh có trong bài tập ý
     */

    public ResponseEntity<?> addCommentsToExercise(Long accountId, Long classroomId, Long exerciseId, CommentRequest commentRequest) {
        var find_classroom_accounts = iAccount_classroomRepository.findAccount_ClassroomsByClassroomId(classroomId);
        for (Account_Classroom account_classroom : find_classroom_accounts) {
//            var check_account = iAccountRepository.findById(account_classroom.getAccountId()).get();
//
//            if (!check_account.getId().equals(accountId)) {
//                return ResponseEntity.badRequest().body(new MessageResponse("this account not exists in the class"));
//            }
//
//            var find_classroom_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomId(classroomId);
//            for (Classroom_Exercise classroom_exercise : find_classroom_exercises) {
//                var check_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
//                if (!check_exercise.getId().equals(exerciseId)) {
//                    return ResponseEntity.badRequest().body(new MessageResponse("this exercise not exists in the class"));
//                }
//            }

            Account_Comment_Exercise account_comment_exercise = new Account_Comment_Exercise();
            account_comment_exercise.setAccountId(accountId);

            // tạo 1 đối tượng commnet
            Comment comment = new Comment();
            comment.setConten(commentRequest.getContent());
            iCommentRepository.save(comment);

            account_comment_exercise.setCommentId(comment.getId());
            account_comment_exercise.setExerciseId(exerciseId);
            account_comment_exercise.setStatus("show");
            account_comment_exercise.setCommnet_date(LocalDate.now());
            iAccount_comment_exerciseRepository.save(account_comment_exercise);
            return ResponseEntity.ok().body(new MessageResponse("comment successfully"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("You cannot comment on this class"));

    }

    public ResponseEntity<?> getAllCommentsToExercise(Long exerciseId) {
        var find_commnets_exercise = iAccount_comment_exerciseRepository.findAccount_Comment_ExercisesByExerciseIdAndStatus(exerciseId, "show");
        Account_Comment_ExerciseResponse account_comment_exerciseResponse = new Account_Comment_ExerciseResponse();
        account_comment_exerciseResponse.setNumber_comment(find_commnets_exercise.size());

        List<Account_CommentResponse> account_commentResponses = new ArrayList<>();
        for (Account_Comment_Exercise account_comment_exercise : find_commnets_exercise) {
            var check_student = iAccountRepository.findById(account_comment_exercise.getAccountId()).get();
            var check_comment = iCommentRepository.findById(account_comment_exercise.getCommentId()).get();
            Account_CommentResponse account_commentResponse = new Account_CommentResponse();
            StudentRequest studentRequest = new StudentRequest(check_student);
            account_commentResponse.setStudent(studentRequest);
            CommentRequest commentRequest = new CommentRequest(check_comment);
            account_commentResponse.setComment(commentRequest);

            account_commentResponse.setComment_date(account_comment_exercise.getCommnet_date());

            account_commentResponses.add(account_commentResponse);
        }
        account_comment_exerciseResponse.setAccount_comments(account_commentResponses);
        return ResponseEntity.ok().body(account_comment_exerciseResponse);
    }

    /*
        check xem commnet ý có phải của mình commnet hay không. Nếu không phải sẽ return luôn
        phải thì có xóa
     */
    public ResponseEntity<?> studentDeleteCommnet(Long accountId, Long exerciseId, Long commentId) {
        var find_student_comment_exercise = iAccount_comment_exerciseRepository.findAccount_Comment_ExercisesByExerciseId(exerciseId);

        // lấy ra danh sách đã comment trong bài tập ý
        for (Account_Comment_Exercise account_comment_exercise : find_student_comment_exercise) {
            var check_account_comment = iAccount_comment_exerciseRepository.findAccount_Comment_ExerciseByCommentId(account_comment_exercise.getCommentId()).get();
            // check xem account ý có comment hay không
            if (check_account_comment.getAccountId().equals(accountId)) {
                if (check_account_comment.getCommentId().equals(commentId)) {
                    iAccount_comment_exerciseRepository.deleteById(check_account_comment.getId());
                    var check_comment = iCommentRepository.findById(commentId).get();
                    iCommentRepository.deleteById(check_comment.getId());
                    return ResponseEntity.ok().body(new MessageResponse("delete comment successfully"));
                }
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("You cannot delete this comment"));
    }

    public ResponseEntity<?> teacherHideCommentsToExercise(Long accountId, Long classroomId, Long exerciseId, Long commentId) {
        var find_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (!find_teacher.getAccountId().equals(accountId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }

        var find_account_comments_exercise = iAccount_comment_exerciseRepository.findAccount_Comment_ExercisesByExerciseId(exerciseId);
        for (Account_Comment_Exercise account_comment_exercise : find_account_comments_exercise) {
            var check_comment = iCommentRepository.findById(account_comment_exercise.getCommentId()).get();
            if (check_comment.getId().equals(commentId)) {
                account_comment_exercise.setStatus("hide");
                iAccount_comment_exerciseRepository.save(account_comment_exercise);
                return ResponseEntity.ok().body(new MessageResponse("hide comment successfully"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("hide comment fail"));
    }

    public ResponseEntity<?> teacherShowCommentsToExercise(Long accountId, Long classroomId, Long exerciseId, Long commentId) {
        var find_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (!find_teacher.getAccountId().equals(accountId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }

        var find_account_comments_exercise = iAccount_comment_exerciseRepository.findAccount_Comment_ExercisesByExerciseId(exerciseId);
        for (Account_Comment_Exercise account_comment_exercise : find_account_comments_exercise) {
            var check_comment = iCommentRepository.findById(account_comment_exercise.getCommentId()).get();
            if (check_comment.getId().equals(commentId)) {
                account_comment_exercise.setStatus("show");
                iAccount_comment_exerciseRepository.save(account_comment_exercise);
                return ResponseEntity.ok().body(new MessageResponse("show comment successfully"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("show comment fail"));
    }

    public ResponseEntity<?> TeacherDeleteCommnet(Long accountId, Long classroomId, Long exerciseId, Long commentId) {
        var find_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (!find_teacher.getAccountId().equals(accountId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }

        var find_student_comment_exercise = iAccount_comment_exerciseRepository.findAccount_Comment_ExercisesByExerciseId(exerciseId);

        // lấy ra danh sách đã comment trong bài tập ý
        for (Account_Comment_Exercise account_comment_exercise : find_student_comment_exercise) {
            var check_account_comment = iAccount_comment_exerciseRepository.findAccount_Comment_ExerciseByCommentId(account_comment_exercise.getCommentId()).get();
            // check xem account ý có comment hay không
            if (check_account_comment.getCommentId().equals(commentId)) {
                iAccount_comment_exerciseRepository.deleteById(check_account_comment.getId());
                var check_comment = iCommentRepository.findById(commentId).get();
                iCommentRepository.deleteById(check_comment.getId());
                return ResponseEntity.ok().body(new MessageResponse("delete comment successfully"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("You cannot delete this comment"));
    }


    /*
        AccountId => Teacher

        exerciseId => lay ra danh sach tat ca comment
        teacher -> commentId thay đôi thuộc tính của nó
     */

}
