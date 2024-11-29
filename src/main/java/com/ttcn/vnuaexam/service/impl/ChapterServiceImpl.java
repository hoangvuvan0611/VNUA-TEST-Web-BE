package com.ttcn.vnuaexam.service.impl;

import com.ttcn.vnuaexam.dto.request.ChapterRequestDto;
import com.ttcn.vnuaexam.dto.response.ChapterResponseDto;
import com.ttcn.vnuaexam.entity.Chapter;
import com.ttcn.vnuaexam.exception.EMException;
import com.ttcn.vnuaexam.repository.ChapterRepository;
import com.ttcn.vnuaexam.service.ChapterService;
import com.ttcn.vnuaexam.service.mapper.ChapterMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ttcn.vnuaexam.constant.enums.ErrorCodeEnum.*;

@Service
@AllArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterMapper chapterMapper;
    private final ChapterRepository chapterRepository;

    @Override
    public ChapterResponseDto getById(Long id) throws EMException {
        // Check id null, trống
        if (ObjectUtils.isEmpty(id)) {
            throw new EMException(CHAPTER_ID_IS_NOT_EXIST);
        }

        var chapter = chapterRepository.findById(id).orElseThrow(() -> new EMException(NOT_FOUND_CHAPTER));
        return chapterMapper.entityToResponse(chapter);
    }

    @Override
    public ChapterResponseDto create(ChapterRequestDto requestDto) throws EMException {
        //validateDepartment
        validateChapter(requestDto, true);

        //Tạo entity
        var chapter = chapterMapper.requestToEntity(requestDto);
        chapterRepository.save(chapter);
        return chapterMapper.entityToResponse(chapter);
    }

    private void validateChapter(ChapterRequestDto requestDto, boolean isCreate) throws EMException {
        // Kiểm tra name
        if(!StringUtils.hasText(requestDto.getName())){
            throw new EMException(CHAPTER_NAME_IS_EMPTY);
        }

        // Kiểm tra name tồn tại trong môn chưa
        List<Chapter> chapters;
        if (isCreate)
            chapters = chapterRepository.findByNameAndSubjectId(requestDto.getName(), requestDto.getSubjectId());
        else
            chapters = chapterRepository.findByNameAndNotId(requestDto.getName(), requestDto.getId(), requestDto.getSubjectId());

        if (!CollectionUtils.isEmpty(chapters))
            throw new EMException(CHAPTER_NAME_IS_EXIST);
    }

    @Override
    public ChapterResponseDto update(ChapterRequestDto requestDto, Long id) throws EMException {
        //lấy ra entity theo id
        var chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EMException(NOT_FOUND));

        //validate request
        validateChapter(requestDto, false);

        //update entity
        chapterMapper.setValue(requestDto, chapter);
        chapter = chapterRepository.save(chapter);
        return chapterMapper.entityToResponse(chapter);
    }

    @Override
    public Boolean deleteById(Long id) throws EMException {
        var department = chapterRepository.findById(id).orElseThrow(() -> new EMException(NOT_FOUND_CHAPTER));
        chapterRepository.delete(department);
        return true;
    }
}