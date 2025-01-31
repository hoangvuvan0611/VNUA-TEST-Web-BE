package com.ttcn.vnuaexam.dto.response;

import com.ttcn.vnuaexam.dto.BaseObjectDto;
import com.ttcn.vnuaexam.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto extends BaseObjectDto implements Serializable {
    private Long id;
    private Long subjectId;
    private Long chapterId;
    private String subjectName;
    private String chapterName;
    private String code;
    private String content;
    private String image;
    private String type;
    private Integer countCorrect;
    List<AnswerResponseDto> answers;
}
