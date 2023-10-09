package com.example.fai_edu_classroom.services;

import com.example.fai_edu_classroom.command.AddStudentCommand;
import com.example.fai_edu_classroom.dto.request.*;
import com.example.fai_edu_classroom.dto.response.*;
import com.example.fai_edu_classroom.entity.Account;
import com.example.fai_edu_classroom.entity.Classroom;
import com.example.fai_edu_classroom.entity.relationship.Account_Classroom;
import com.example.fai_edu_classroom.entity.relationship.Account_Exercise;
import com.example.fai_edu_classroom.entity.relationship.Classroom_Exercise;
import com.example.fai_edu_classroom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ClassroomService {
    @Autowired
    IClassroomRepository iClassroomRepository;

    @Autowired
    IAccountRepository iAccountRepository;

    @Autowired
    IAccount_ClassroomRepository iAccount_classroomRepository;

    @Autowired
    IClassroom_ExerciseRepository iClassroom_exerciseRepository;

    @Autowired
    IExerciseRepository iExerciseRepository;

    @Autowired
    IAccount_ExerciseRepository iAccount_exerciseRepository;

    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
    private static Random generator = new Random();

    public static int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    /*
        => TEST OK
     */
    public ResponseEntity<?> createClassroom(Long accountId, ClassroomRequest classroomRequest) {
        var find_account_id = iAccountRepository.findById(accountId);
        if (find_account_id.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("accountId not exists"));
        }
        Account account = find_account_id.get();
        Classroom classroom = new Classroom();
        classroom.setClassroomName(classroomRequest.getClassroomName());
        StringBuilder sb = new StringBuilder();
        int numberOfCharactor = 10;
        for (int i = 0; i < numberOfCharactor; i++) {
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        String randomString = sb.toString();
        classroom.setCode(randomString);
        classroom.setTopic(classroomRequest.getTopic());
        classroom.setRoom(classroomRequest.getRoom());
        iClassroomRepository.save(classroom);

        Account_Classroom account_classroom = new Account_Classroom();
        account_classroom.setAccountId(account.getId());
        account_classroom.setClassroomId(classroom.getId());
        account_classroom.setPosition("teacher");
        iAccount_classroomRepository.save(account_classroom);
        return ResponseEntity.ok().body(new MessageResponse("create classroom successfully"));

    }

    public ResponseEntity<?> updateClassroom(Long id) {
        var find_classroom = iClassroomRepository.findById(id);
        if (find_classroom.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Classroom Update Failed"));
        }
        Classroom classroom = find_classroom.get();
        classroom.setClassroomName(classroom.getClassroomName());
        classroom.setTopic(classroom.getTopic());
        classroom.setRoom(classroom.getRoom());
        iClassroomRepository.save(classroom);
        return ResponseEntity.ok().body(new MessageResponse("create classroom successfully"));
    }

    /*
        nếu mà student xin tham gia vào lớp thì sẽ là trạng thái "chờ"

        => test OK
     */
    public ResponseEntity<?> attendClassroomByCode(Long accountId, ClassroomRequest classroomRequest) {
        var find_classroom_code = iClassroomRepository.findByCode(classroomRequest.getCode());
        if (find_classroom_code.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("classroom code not exists"));
        }
        Classroom classroom = find_classroom_code.get();

        var find_account = iAccountRepository.findById(accountId);
        if (find_account.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("account not exists"));
        }
        Account account = find_account.get();

        Account_Classroom account_classroom = new Account_Classroom();
        account_classroom.setAccountId(account.getId());
        account_classroom.setClassroomId(classroom.getId());
        account_classroom.setPosition("awaiting");
        var find_classroom_accounts = iAccount_classroomRepository.findAccount_ClassroomsByClassroomId(classroom.getId());
        for (Account_Classroom ac : find_classroom_accounts) {
            if (ac.getAccountId().equals(account_classroom.getAccountId()) && ac.getClassroomId().equals(account_classroom.getClassroomId())) {
                return ResponseEntity.badRequest().body(new MessageResponse("this account already exists in the classroom"));
            }
        }
        iAccount_classroomRepository.save(account_classroom);
        return ResponseEntity.ok().body(new MessageResponse("attend classroom successfully"));
    }

    /*
        => Test Ok
     */
    public ResponseEntity<?> confirmAttendClassroomByCode(Long accountId, Long classroomId, AddStudentCommand addStudentCommand) {
        var find_classroom_account_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (!accountId.equals(find_classroom_account_teacher.getAccountId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }
        // lấy ra danh sách đang chờ
        List<StudentRequest> studentRequests = addStudentCommand.getStudents();
        for (StudentRequest studentRequest : studentRequests) {
            var check_student_request = iAccountRepository.findById(studentRequest.getStudentId()).get();

            var find_student_awaiting = iAccount_classroomRepository.findAccount_ClassroomsByClassroomIdAndPosition(classroomId, "awaiting");
            for (Account_Classroom account_classroom : find_student_awaiting) {
                // Nếu mà account_classroom khong co account id y thi se return
                if (!check_student_request.getId().equals(account_classroom.getAccountId())) {
                    return ResponseEntity.badRequest().body(new MessageResponse(check_student_request.getUsername() + " not exists in the classroom awaiting"));
                }
                account_classroom.setPosition("student");
                iAccount_classroomRepository.save(account_classroom);

                // MỚI THÊM
                var find_classroom_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomId(classroomId);
                for (Classroom_Exercise classroom_exercise : find_classroom_exercises){
                    var check_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
                    Account_Exercise account_exercise = new Account_Exercise();
                    account_exercise.setExerciseId(check_exercise.getId());
                    account_exercise.setAccountId(check_student_request.getId());
                    iAccount_exerciseRepository.save(account_exercise);
                }
            }
        }
        return ResponseEntity.ok().body(new MessageResponse("approved student for the class"));
    }

    /*
        lấy danh sách account đang chờ duyệt vào lớp
        => Test Ok
     */
    public ResponseEntity<?> getAttendClassroomByAwaiting(Long accountId, Long classroomId) {
        var find_classroom_account_teacher = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (!accountId.equals(find_classroom_account_teacher.getAccountId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("this account is not a teacher"));
        }

        StudentAwaitingResponse studentAwaitingResponse = new StudentAwaitingResponse();
        List<StudentRequest> studentRequests = new ArrayList<>();
        var find_students_awaiting = iAccount_classroomRepository.findAccount_ClassroomsByClassroomIdAndPosition(classroomId, "awaiting");
        for (Account_Classroom account_classroom : find_students_awaiting) {
            var check_student = iAccountRepository.findById(account_classroom.getAccountId()).get();
            StudentRequest studentRequest = new StudentRequest(check_student);
            studentRequests.add(studentRequest);
        }
        studentAwaitingResponse.setNumber_awaiting(find_students_awaiting.size());
        studentAwaitingResponse.setStudents(studentRequests);
        return ResponseEntity.ok().body(studentAwaitingResponse);
    }

    /*
        Có thể add nhiều học sinh vào lớp cùng 1 lúc
        => Test ok
     */
    public ResponseEntity<?> addStudentToClassroom(Long accountId, Long classroomId, StudentRequest studentRequest) {
        var find_account = iAccountRepository.findById(accountId).get();
        var find_classroom = iClassroomRepository.findById(classroomId).get();
        // kiểm tra xem account này có phải là giáo viên hay không ?
        var find_account_teacher_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(find_classroom.getId(), "teacher").get();
        if (!find_account_teacher_classroom.getAccountId().equals(find_account.getId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("This account is not a teacher"));
        }

        // tìm ra được danh sách account có trong lớp
        var find_classroom_accounts = iAccount_classroomRepository.findAccount_ClassroomsByClassroomId(find_classroom.getId());

        var check_student_request = iAccountRepository.findByUsername(studentRequest.getUsername()).get();
        // duyệt danh sách trong account classroom check xem có tồn tại hay không
        for (Account_Classroom ac : find_classroom_accounts) {
            if (ac.getAccountId().equals(check_student_request.getId())) {
                return ResponseEntity.badRequest().body(new MessageResponse(check_student_request.getUsername() + " already exists in the classroom"));
            }
        }
        Account_Classroom account_classroom = new Account_Classroom();
        account_classroom.setAccountId(check_student_request.getId());
        account_classroom.setClassroomId(find_classroom.getId());
        account_classroom.setPosition("student");
        iAccount_classroomRepository.save(account_classroom);

        // MỚI THÊM
        var find_classroom_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomId(find_classroom.getId());
        for (Classroom_Exercise classroom_exercise : find_classroom_exercises){
            var check_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
            Account_Exercise account_exercise = new Account_Exercise();
            account_exercise.setExerciseId(check_exercise.getId());
            account_exercise.setAccountId(check_student_request.getId());
            iAccount_exerciseRepository.save(account_exercise);
        }

        return ResponseEntity.ok().body(new MessageResponse("add student successfully"));
    }

    /*
        # test Ok
     */
    public ResponseEntity<?> searchStudent(String query) {
        StudentResponse studentResponse = new StudentResponse();
        List<StudentRequest> studentRequests = new ArrayList<>();
        List<Account> accounts = iAccountRepository.searchAccounts(query);
        for (Account account : accounts) {
            var check_acocunt = iAccountRepository.findById(account.getId()).get();
            StudentRequest studentRequest = new StudentRequest(check_acocunt);
            studentRequests.add(studentRequest);
        }
        studentResponse.setStudents(studentRequests);

        return ResponseEntity.ok().body(studentResponse);
    }

    /*
        hien thi danh sach giao vien va hoc sinh
        => Test OK
     */
    public ResponseEntity<?> getStudentsFromClassroom(Long id) {
        var find_classroom_id = iClassroomRepository.findById(id);
        if (find_classroom_id.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("classroom not exists"));
        }
        Classroom classroom = find_classroom_id.get();
        var find_classroom_accounts = iAccount_classroomRepository.findAccount_ClassroomsByClassroomId(classroom.getId());
        Attend_Account_ClassroomResponse attend_account_classroomResponse = new Attend_Account_ClassroomResponse();
        attend_account_classroomResponse.setClassroomId(classroom.getId());
        attend_account_classroomResponse.setClassroomName(classroom.getClassroomName());
        List<StudentRequest> studentRequests = new ArrayList<>();
        for (Account_Classroom account_classroom : find_classroom_accounts) {
            var check_account = iAccountRepository.findById(account_classroom.getAccountId()).get();
            if (account_classroom.getPosition().equalsIgnoreCase("teacher")) {
                TeacherRequest teacherRequest = new TeacherRequest(check_account);
                attend_account_classroomResponse.setTeacher(teacherRequest);
            } else {
                StudentRequest studentRequest = new StudentRequest(check_account);
                studentRequests.add(studentRequest);
                int number = studentRequests.size();
                attend_account_classroomResponse.setNumber(number);
                attend_account_classroomResponse.setStudents(studentRequests);
            }
        }
        return ResponseEntity.ok().body(attend_account_classroomResponse);
    }

    /*
        # test OK
     */
    public ResponseEntity<?> deleteStudentFromClassroom(Long accountId, Long classroomId, Long studentId) {
        var find_classroom = iClassroomRepository.findById(classroomId);
        if (find_classroom.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("classroom not exists"));
        }
        Classroom classroom = find_classroom.get();
        // tìm teacher cho classroom
        var find_account_teacher_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndPosition(classroomId, "teacher").get();
        if (!find_account_teacher_classroom.getAccountId().equals(accountId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("This account cannot delete students in this class"));
        }

        // chỉ được xóa sinh viên trong lớp này
        var find_classroom_account_students = iAccount_classroomRepository.findAccount_ClassroomsByClassroomIdAndPosition(classroom.getId(), "student");
        for (Account_Classroom account_classroom : find_classroom_account_students) {
            var find_student = iAccountRepository.findById(studentId).get();
            var check_student_classroom = iAccount_classroomRepository.findAccount_ClassroomByClassroomIdAndAccountId(classroom.getId(), find_student.getId()).get();
            if (account_classroom.getAccountId().equals(find_student.getId())) {
                iAccount_classroomRepository.deleteById(check_student_classroom.getId());
                return ResponseEntity.ok().body(new MessageResponse("delete student from class successfully"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("delete student from class faild"));
    }


    /*
        => Test Ok
     */
    public ResponseEntity<?> getExecisesToClassroom(Long id) {
        var find_classroom = iClassroomRepository.findById(id);
        if (find_classroom.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("classroom not exists"));
        }
        Classroom classroom = find_classroom.get();
        // lấy ra danh sách bài tập trong lớp ý
        var find_classroom_exercises = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomId(classroom.getId());
        List<ExerciseRequest> exerciseRequests = new ArrayList<>();
        Classroom_ExerciseResponse classroom_exerciseResponse = new Classroom_ExerciseResponse();
        classroom_exerciseResponse.setClassroomId(classroom.getId());
        classroom_exerciseResponse.setClassroomName(classroom.getClassroomName());
        for (Classroom_Exercise ce : find_classroom_exercises) {
            var check_exercise = iExerciseRepository.findById(ce.getExerciseId()).get();
            ExerciseRequest exerciseRequest = new ExerciseRequest(check_exercise);
            exerciseRequests.add(exerciseRequest);
        }
        classroom_exerciseResponse.setExercises(exerciseRequests);
        return ResponseEntity.ok().body(classroom_exerciseResponse);
    }

    /*
        => Test Ok
     */
    public ResponseEntity<?> getClassroomsToAccount(Long id) {
        var find_account = iAccountRepository.findById(id).get();
        var find_account_classrooms = iAccount_classroomRepository.findAccountClassroomByPosition("teacher", "student", find_account.getId());
        List<ClassroomRequest> classroomRequests = new ArrayList<>();
        Account_ClassroomsResponse account_classroomsResponse = new Account_ClassroomsResponse();
        for (Account_Classroom account_classroom : find_account_classrooms) {
            var check_classroom = iClassroomRepository.findById(account_classroom.getClassroomId()).get();
            ClassroomRequest classroomRequest = new ClassroomRequest(check_classroom);
            classroomRequests.add(classroomRequest);
        }
        account_classroomsResponse.setClassrooms(classroomRequests);
        return ResponseEntity.ok().body(account_classroomsResponse);
    }

    /*
        => Test Ok
     */
    public ResponseEntity<?> getClassroomById(Long id) {
        var find_classroom = iClassroomRepository.findById(id);
        if (find_classroom.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("classroom not exists"));
        }
        Classroom classroom = find_classroom.get();
        return ResponseEntity.ok().body(classroom);
    }

    public ResponseEntity<?> deleteClassroomById(Long id) {
        Optional<Classroom> optional = iClassroomRepository.findById(id);
        if (optional.isPresent()) {
            iClassroomRepository.deleteById(id);
            return ResponseEntity.ok().body(new MessageResponse("Classroom Delete Success"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Classroom Delete Failed"));
    }

    public ResponseEntity<?> getDetailExerciseToClassroom(Long classroomId, Long exerciseId) {
        var find_exercises_classroom = iClassroom_exerciseRepository.findClassroom_ExercisesByClassroomId(classroomId);
        for (Classroom_Exercise classroom_exercise : find_exercises_classroom){
            var find_exercise = iExerciseRepository.findById(classroom_exercise.getExerciseId()).get();
            if (find_exercise.getId().equals(exerciseId)){

                ExerciseRequest exerciseRequest = new ExerciseRequest(find_exercise);
                return ResponseEntity.ok().body(exerciseRequest);
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("get detail exercise faild"));
    }
}
