package com.ttcn.vnuaexam.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportExcelService {
    String importStudent(MultipartFile file) throws IOException;
}
