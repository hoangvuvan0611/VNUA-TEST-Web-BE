package com.ttcn.vnuaexam.service.impl;

import com.ttcn.vnuaexam.constant.enums.ErrorCodeEnum;
import com.ttcn.vnuaexam.constant.enums.TypeAnswerEnum;
import com.ttcn.vnuaexam.corollary.ExamResultSetResponse;
import com.ttcn.vnuaexam.dto.request.ExamRequestDto;
import com.ttcn.vnuaexam.dto.response.AnswerResponseDto;
import com.ttcn.vnuaexam.dto.response.ExamResponseDto;
import com.ttcn.vnuaexam.dto.response.QuestionResponseDto;
import com.ttcn.vnuaexam.dto.search.ExamSearchDto;
import com.ttcn.vnuaexam.entity.Exam;
import com.ttcn.vnuaexam.entity.ExamQuestion;
import com.ttcn.vnuaexam.exception.EMException;
import com.ttcn.vnuaexam.repository.AnswerRepository;
import com.ttcn.vnuaexam.repository.ExamQuestionRepository;
import com.ttcn.vnuaexam.repository.ExamRepository;
import com.ttcn.vnuaexam.repository.SubjectRepository;
import com.ttcn.vnuaexam.service.AnswerService;
import com.ttcn.vnuaexam.service.ExamService;
import com.ttcn.vnuaexam.service.QuestionService;
import com.ttcn.vnuaexam.service.mapper.AnswerMapper;
import com.ttcn.vnuaexam.service.mapper.ExamMapper;
import com.ttcn.vnuaexam.service.mapper.SubjectMapper;
import com.ttcn.vnuaexam.utils.PageUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.emitter.EmitterException;

import java.util.*;
import java.util.stream.Collectors;

import static com.ttcn.vnuaexam.constant.enums.ErrorCodeEnum.NOT_FOUND;
import static com.ttcn.vnuaexam.constant.enums.StatusExamEnum.DOING;

@AllArgsConstructor
@Service
@Transactional
public class ExamServiceImpl implements ExamService {
    private final QuestionService questionService;
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final ExamQuestionRepository examQuestionRepository;
    private final SubjectRepository subjectRepository;
    private final AnswerService answerService;
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    @Override
    public ExamResponseDto getById(Long id) throws EMException {
        // Lay de thi
        var exam = examRepository.findById(id).orElseThrow(() -> new EMException(NOT_FOUND));

        //Lay danh sach id cau hoi
        var examQuestions = examQuestionRepository.findByExamId(exam.getId());
        List<Long> questionIds = examQuestions.stream().map(ExamQuestion::getQuestionId).toList();

        //Lay cau hoi theo list id(service)
        var questionsResponses = questionService.getAllByIds(questionIds);

        // Lay dap an
        if (!questionsResponses.isEmpty()) {
            for (QuestionResponseDto question : questionsResponses) {
                var answersEntity = answerRepository.findByQuestionId(question.getId());
                var answersResponse = answersEntity.stream().map(answerMapper::entityToResponse).collect(Collectors.toList());

                // de cau allAnswer ở cuối
                AnswerResponseDto lastAnswer = answersResponse.stream().filter(a -> a.getType() != null && a.getType()
                        .equals(TypeAnswerEnum.LAST_ANSWER.getCode())).findFirst().orElse(null);

                if (lastAnswer != null) {
                    answersResponse.remove(lastAnswer);
                    Collections.shuffle(answersResponse);
                    answersResponse.add(lastAnswer);
                } else {
                    Collections.shuffle(answersResponse);
                }

                question.setAnswers(answersResponse);
            }
        }

        // tra ra exam
        var result = examMapper.entityToResponse(exam);
        Collections.shuffle(questionsResponses);
        result.setQuestions(questionsResponses);

        return result;
    }


    @Override
    public ExamResponseDto create(ExamRequestDto examRequestDto) throws EMException {
        // validate request
        validateExam(examRequestDto);

        //Tạo mới Exam
        var newExam = examMapper.requestToEntity(examRequestDto);
        examRepository.save(newExam);
        if (!examRequestDto.getQuestionIds().isEmpty()) {
            saveQuestion(newExam.getId(), examRequestDto.getQuestionIds());
        }
        return examMapper.entityToResponse(newExam);
    }

    @Override
    public ExamResponseDto update(ExamRequestDto examRequestDto, Long examId) throws EMException {
        //validate request

        // update exam
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new EMException(NOT_FOUND));
        examMapper.setValue(examRequestDto, exam);
        if (!examRequestDto.getQuestionIds().isEmpty()) {
            saveQuestion(examId, examRequestDto.getQuestionIds());
        }
        return examMapper.entityToResponse(exam);
    }

    private void validateExam(ExamRequestDto examRequestDto) throws EMException {
        if (!subjectRepository.existsById(examRequestDto.getSubjectId())) {
            throw new EMException(ErrorCodeEnum.SUBJECT_ID_IS_NOT_EXIST);
        }
    }

    @Override
    public ErrorCodeEnum saveQuestion(Long examId, List<Long> questionIds) throws EMException {
        var exam = examRepository.findById(examId).orElseThrow(() -> new EMException(NOT_FOUND));
        for (var questionId : questionIds) {
            if (isDuplicateQuestion(exam, questionId))
                continue;
            examQuestionRepository.save(new ExamQuestion(exam.getId(), questionId));
        }
        examRepository.save(exam);
        return ErrorCodeEnum.SUCCESS;
    }

    private Boolean isDuplicateQuestion(Exam exam, Long questionId) {
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamId(exam.getId());
        return examQuestions.stream()
                .anyMatch(eq -> eq.getQuestionId().equals(questionId));
    }

    @Override
    public Page<ExamResponseDto> search(ExamSearchDto searchDto) {
        Pageable pageRequest = PageUtils.getPageable(searchDto.getPageIndex(), searchDto.getPageSize());
        Page<ExamResultSetResponse> examResult = examRepository.search(searchDto, pageRequest);
        return examResult.map(ExamResponseDto::new);
    }

    @Override
    public Boolean deleteById(Long id) throws EMException {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new EMException(NOT_FOUND));
        examQuestionRepository.deleteByExamId(id);
        examRepository.deleteById(id);
        return true;
    }
}