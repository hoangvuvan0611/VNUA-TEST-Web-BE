package com.ttcn.vnuaexam.rest;

import com.ttcn.vnuaexam.dto.request.ExamRequestDto;
import com.ttcn.vnuaexam.dto.response.ExamResponseDto;
import com.ttcn.vnuaexam.exception.EMException;
import com.ttcn.vnuaexam.response.EMResponse;
import com.ttcn.vnuaexam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class RestExamController {
    private final ExamService examService;

    @GetMapping("/{id}")
    public EMResponse<ExamResponseDto> getById(@PathVariable Long id) throws EMException {
        return new EMResponse<>(examService.getById(id));
    }

    @PostMapping()
    public EMResponse<ExamResponseDto> create(@RequestBody ExamRequestDto requestDto) throws EMException {
        return new EMResponse<>(examService.create(requestDto));
    }

    @PutMapping("/{id}")
    public EMResponse<ExamResponseDto> update(@PathVariable Long id,
                                              @RequestBody ExamRequestDto requestDto) throws EMException {
        return new EMResponse<>(examService.update(requestDto, id));
    }

    @PostMapping("/question")
    public EMResponse<String> saveQuestion(@RequestBody ExamRequestDto requestDto) throws EMException {
        return new EMResponse<>(examService.saveQuestion(requestDto.getId(), requestDto.getQuestionIds()));
    }
}
