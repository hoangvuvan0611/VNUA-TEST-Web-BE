package com.ttcn.vnuaexam.service.impl;

import com.ttcn.vnuaexam.constant.Constant;
import com.ttcn.vnuaexam.dto.request.UserRequestDto;
import com.ttcn.vnuaexam.service.ImportExcelService;
import com.ttcn.vnuaexam.service.RoomStudentService;
import com.ttcn.vnuaexam.service.UserService;
import com.ttcn.vnuaexam.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ttcn.vnuaexam.constant.MessageCodes.EXCEL_EXTENSION_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportExcelServiceImpl implements ImportExcelService {
    private final UserService userService;
    private final RoomStudentService roomStudentService;

    @Override
    public String importStudent(MultipartFile file) throws IOException {
        // tạo message
        StringBuilder message = new StringBuilder();
        Workbook workbook = null; // <-Interface, accepts both HSSF and XSSF.
        Sheet sheet;

        // Duyệt file
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes())) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            switch (Objects.requireNonNull(extension)) {
                case Constant.EXCEL_EXTENSION.XLSX:
                    workbook = new XSSFWorkbook(byteArrayInputStream);
                    sheet = workbook.getSheetAt(0);
                    break;
                case Constant.EXCEL_EXTENSION.XLS:
                    workbook = new HSSFWorkbook(byteArrayInputStream);
                    sheet = workbook.getSheetAt(0);
                    break;
                default:
                    return EXCEL_EXTENSION_ERROR_MESSAGE;
            }
            if (sheet != null) {
                List<UserRequestDto> requestDtoList = getDataFromFile(sheet);
                message.append(userService.importListStudent(requestDtoList));
            }
        } catch (IOException e) {
            log.error("False to file Nhap_sinh_vien : ERROR: {}", e.getMessage(), e);
        } finally {
            if (workbook != null)
                workbook.close();
        }
        return message.toString();
    }

    private List<UserRequestDto> getDataFromFile(Sheet sheet) {
        List<UserRequestDto> results = new ArrayList<>();

        int cellIndex;
        for (Row currentRow : sheet) {
            UserRequestDto requestDto = new UserRequestDto();
            int rowNum = currentRow.getRowNum();

            if (rowNum == 0) {
                continue;
            }

            if (ExcelUtils.isRowEmpty(currentRow)) {
                continue;
            }

            cellIndex = 0;
            requestDto.setCode(ExcelUtils.getCellValue(currentRow.getCell(cellIndex++)));
            requestDto.setFullName(ExcelUtils.getCellValue(currentRow.getCell(cellIndex++)));
            requestDto.setClassCode(ExcelUtils.getCellValue(currentRow.getCell(cellIndex++)));
            requestDto.setDob(ExcelUtils.getCellValue(currentRow.getCell(cellIndex++)));
            requestDto.setAddress(ExcelUtils.getCellValue(currentRow.getCell(cellIndex)));
            results.add(requestDto);
        }
        return results;
    }

    @Override
    public String addStudentToRoom(MultipartFile file, Long roomId) throws IOException {
        StringBuilder message = new StringBuilder();
        Workbook workbook = null;
        Sheet sheet;

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes())) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            switch (Objects.requireNonNull(extension)) {
                case Constant.EXCEL_EXTENSION.XLSX:
                    workbook = new XSSFWorkbook(byteArrayInputStream);
                    sheet = workbook.getSheetAt(0);
                    break;
                case Constant.EXCEL_EXTENSION.XLS:
                    workbook = new HSSFWorkbook(byteArrayInputStream);
                    sheet = workbook.getSheetAt(0);
                    break;
                default:
                    return EXCEL_EXTENSION_ERROR_MESSAGE;
            }

            if (sheet != null) {
                var studentCode = getStudentCodeFromFile(sheet);
                message.append(roomStudentService.addStudentToRoom(studentCode, roomId));
            }
        } catch (IOException e) {
            log.error("False to file Nhap_sinh_vien : ERROR: {}", e.getMessage(), e);
        } finally {
            if (workbook != null)
                workbook.close();
        }
        return message.toString();
    }

    private List<String> getStudentCodeFromFile(Sheet sheet) {
        List<String> listCode = new ArrayList<>();

        int cellIndex;
        for (Row currentRow : sheet) {
            int rowNum = currentRow.getRowNum();
            if (rowNum == 0) {
                continue;
            }
            if (ExcelUtils.isRowEmpty(currentRow)) {
                continue;
            }

            cellIndex = 0;
            listCode.add(ExcelUtils.getCellValue(currentRow.getCell(cellIndex)));
        }
        return listCode;
    }

    @Override
    public String importQuestion(MultipartFile file) throws IOException {
        // tạo message
        StringBuilder message = new StringBuilder();
        Workbook workbook = null; // <-Interface, accepts both HSSF and XSSF.
        Sheet sheet;

        // Duyệt file
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes())) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            switch (Objects.requireNonNull(extension)) {
                case Constant.EXCEL_EXTENSION.XLSX:
                    workbook = new XSSFWorkbook(byteArrayInputStream);
                    sheet = workbook.getSheetAt(0);
                    break;
                case Constant.EXCEL_EXTENSION.XLS:
                    workbook = new HSSFWorkbook(byteArrayInputStream);
                    sheet = workbook.getSheetAt(0);
                    break;
                default:
                    return EXCEL_EXTENSION_ERROR_MESSAGE;
            }
            if (sheet != null) {
                List<UserRequestDto> requestDtoList = getDataFromFile(sheet);
                for (UserRequestDto userRequestDto : requestDtoList) {

                }
            }
        } catch (IOException e) {
            log.error("False to importShippingStatus : ERROR: {}", e.getMessage(), e);
        } finally {
            if (workbook != null)
                workbook.close();
        }
        return message.toString();
    }
}
