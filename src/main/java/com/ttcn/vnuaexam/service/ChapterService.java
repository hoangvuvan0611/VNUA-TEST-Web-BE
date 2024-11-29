package com.ttcn.vnuaexam.service;

import com.ttcn.vnuaexam.dto.request.ChapterRequestDto;
import com.ttcn.vnuaexam.dto.response.ChapterResponseDto;
import com.ttcn.vnuaexam.dto.search.DepartmentSearchDto;
import com.ttcn.vnuaexam.exception.EMException;
import org.springframework.data.domain.Page;

public interface ChapterService {
    ChapterResponseDto getById(Long id) throws EMException;

    ChapterResponseDto create(ChapterRequestDto requestDto) throws EMException;

    ChapterResponseDto update(ChapterRequestDto requestDto, Long id) throws EMException;

    Boolean deleteById(Long id) throws EMException;
}