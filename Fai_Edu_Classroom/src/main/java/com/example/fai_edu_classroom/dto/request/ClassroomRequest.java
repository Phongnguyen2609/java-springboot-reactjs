package com.example.fai_edu_classroom.dto.request;

import com.example.fai_edu_classroom.entity.Classroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequest {
    private Long classroomId;
    private String classroomName;

    private String code;

    private String topic;

    private String room;

    public ClassroomRequest(Classroom classroom){
        this.classroomId = classroom.getId();
        this.classroomName = classroom.getClassroomName();
        this.code = classroom.getCode();
        this.topic = classroom.getTopic();
        this.room = classroom.getRoom();
    }
}
