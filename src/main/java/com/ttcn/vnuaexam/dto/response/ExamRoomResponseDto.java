package com.ttcn.vnuaexam.dto.response;

import com.ttcn.vnuaexam.constant.enums.StatusExamRoomEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamRoomResponseDto {
    private Long id;
    private Long examId;
    private Long teacherId;
    private String room;
    private String code;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime sessionDate;
    private String status;

    public void setStatus(Integer statusCode) {
        if (statusCode != null)
            this.status = StatusExamRoomEnum.fromCode(statusCode).getValue();
    }

    public ExamRoomResponseDto(Long id, Long examId,  Long teacherId, String room, LocalDateTime startTime,
                               LocalDateTime endTime, Integer status) {
        this.examId = examId;
        this.id = id;
        this.teacherId = teacherId;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.setStatus(status);
    }
}