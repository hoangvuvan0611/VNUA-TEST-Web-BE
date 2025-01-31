package com.ttcn.vnuaexam.rest;

import com.ttcn.vnuaexam.dto.request.SemesterRequestDto;
import com.ttcn.vnuaexam.dto.response.SemesterResponseDto;
import com.ttcn.vnuaexam.dto.search.SearchDto;
import com.ttcn.vnuaexam.exception.EMException;
import com.ttcn.vnuaexam.response.EMResponse;
import com.ttcn.vnuaexam.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class RestSemesterController {
    private final SemesterService semesterService;

    @GetMapping("/{id}")
    public EMResponse<SemesterResponseDto> getById(@PathVariable("id") Long id) throws EMException {
        return new EMResponse<>(semesterService.getById(id));
    }

    @PostMapping
    public EMResponse<SemesterResponseDto> create(@RequestBody SemesterRequestDto requestDto) {
        return new EMResponse<>(semesterService.create(requestDto));
    }

    @PutMapping("/{id}")
    public EMResponse<SemesterResponseDto> update(@PathVariable("id") Long id, @RequestBody SemesterRequestDto requestDto) throws EMException {
        return new EMResponse<>(semesterService.update(id, requestDto));
    }

    @GetMapping("/search")
    public EMResponse<Page<SemesterResponseDto>> search(SearchDto dto) {
        return new EMResponse<>(semesterService.search(dto));
    }
}
