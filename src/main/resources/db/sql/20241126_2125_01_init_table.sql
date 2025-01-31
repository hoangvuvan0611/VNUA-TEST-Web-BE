-- Tạo database nếu chưa tồn tại
-- CREATE DATABASE IF NOT EXISTS exam_management
--  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
-- Bộ môn
--
-- Tắt kiểm tra khóa ngoại
SET foreign_key_checks = 0;

-- Môn
DROP TABLE IF EXISTS tbl_subject;
CREATE TABLE tbl_subject (
                             id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
														 create_date DATETIME NOT NULL,
                             created_by VARCHAR(255) NOT NULL,
                             modify_date DATETIME NULL DEFAULT NULL,
                             modified_by VARCHAR(255) NULL DEFAULT NULL,
                             code VARCHAR(255) NULL DEFAULT NULL,
                             name VARCHAR(255) NULL DEFAULT NULL,
                             description VARCHAR(255) NULL DEFAULT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Chương
DROP TABLE IF EXISTS tbl_chapter;
CREATE TABLE tbl_chapter (
                                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
								create_date DATETIME NOT NULL,
								created_by VARCHAR(255) NOT NULL,
								modify_date DATETIME NULL DEFAULT NULL,
								modified_by VARCHAR(255) NULL DEFAULT NULL,
								subject_id BIGINT NULL DEFAULT NULL,
                                name VARCHAR(255) NULL DEFAULT NULL,														
                                description VARCHAR(255) NULL DEFAULT NULL,
																FOREIGN KEY (subject_id) REFERENCES tbl_subject(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Câu hỏi
DROP TABLE IF EXISTS tbl_question;
CREATE TABLE tbl_question (
                              id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              subject_id BIGINT NULL DEFAULT NULL,
							  chapter_id BIGINT NULL DEFAULT NULL,
                              create_date DATETIME NOT NULL,
                              created_by VARCHAR(255) NOT NULL,
                              modify_date DATETIME NULL DEFAULT NULL,
                              modified_by VARCHAR(255) NULL DEFAULT NULL,
                              content VARCHAR(255) NULL DEFAULT NULL,
                              image VARCHAR(255) NULL DEFAULT NULL,
							  count_correct INT NULL DEFAULT NULL,
                              FOREIGN KEY (subject_id) REFERENCES tbl_subject(id),
							  FOREIGN KEY (chapter_id) REFERENCES tbl_chapter(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Đáp án
DROP TABLE IF EXISTS tbl_answer;
CREATE TABLE tbl_answer (
                            id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            create_date DATETIME NOT NULL,
                            created_by VARCHAR(255) NOT NULL,
                            modify_date DATETIME NULL DEFAULT NULL,
                            modified_by VARCHAR(255) NULL DEFAULT NULL,
                            question_id BIGINT NULL DEFAULT NULL,
														order_number INT NULL DEFAULT NULL,
                            content VARCHAR(255) NULL DEFAULT NULL,
														image VARCHAR(255) NULL DEFAULT NULL,
                            is_correct BIT(1) NOT NULL,
                            FOREIGN KEY (question_id) REFERENCES tbl_question(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Đề thi
DROP TABLE IF EXISTS tbl_exam;
CREATE TABLE tbl_exam (
                          id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          subject_id BIGINT NULL DEFAULT NULL,
                          created_by VARCHAR(255) NOT NULL,
                          create_date DATETIME NOT NULL,
                          modified_by VARCHAR(255) NULL DEFAULT NULL,
                          modify_date DATETIME NULL DEFAULT NULL,
						  code VARCHAR(255) NULL DEFAULT NULL,
                          title VARCHAR(255) NULL DEFAULT NULL,
                          description VARCHAR(255) NULL DEFAULT NULL,
                          duration INT NULL DEFAULT NULL,
                          total_questions INT NULL DEFAULT NULL,
						  total_score DECIMAL(10,2) NULL DEFAULT NULL,
                          status INT NULL DEFAULT NULL,
                          exam_date DATETIME NULL DEFAULT NULL,
                          FOREIGN KEY (subject_id) REFERENCES tbl_subject(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Đề thi và câu hỏi
DROP TABLE IF EXISTS tbl_exam_question;
CREATE TABLE tbl_exam_question (
                                   id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   exam_id BIGINT NULL DEFAULT NULL,
                                   question_id BIGINT NULL DEFAULT NULL,
								   question_order INT NULL DEFAULT NULL,
                                   FOREIGN KEY (exam_id) REFERENCES tbl_exam(id),
                                   FOREIGN KEY (question_id) REFERENCES tbl_question(id),
								   UNIQUE KEY unique_exam_question (exam_id, question_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Người dùng
DROP TABLE IF EXISTS tbl_user;
CREATE TABLE tbl_user (
                          id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          created_by VARCHAR(255) NOT NULL,
                          create_date DATETIME NOT NULL,
                          modified_by VARCHAR(255) NULL DEFAULT NULL,
                          modify_date DATETIME NULL DEFAULT NULL,
                          username VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          code VARCHAR(50) NULL DEFAULT NULL,
													class_code VARCHAR(50) DEFAULT NULL,
													address VARCHAR(255) DEFAULT NULL,
													dob DATETIME DEFAULT NULL,
                          full_name VARCHAR(255) NULL DEFAULT NULL,
                          role INT NULL DEFAULT NULL,
                          subject_id BIGINT NULL DEFAULT NULL,
                          is_active BIT(1) NULL DEFAULT NULL,
                          FOREIGN KEY (subject_id) REFERENCES tbl_subject(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Lớp
DROP TABLE IF EXISTS tbl_class;
CREATE TABLE tbl_class (
                             id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
							 create_date DATETIME NOT NULL,
                             created_by VARCHAR(255) NOT NULL,
                             modify_date DATETIME NULL DEFAULT NULL,
                             modified_by VARCHAR(255) NULL DEFAULT NULL,
														 subject_id BIGINT NULL DEFAULT NULL,
                             code VARCHAR(255) NULL DEFAULT NULL,
                             name VARCHAR(255) NULL DEFAULT NULL,
                             description VARCHAR(255) NULL DEFAULT NULL,
														 FOREIGN KEY (subject_id) REFERENCES tbl_subject(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;


-- Lớp học- sinh viên  
DROP TABLE IF EXISTS tbl_class_user;
CREATE TABLE tbl_class_user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    class_id BIGINT NOT NULL,
		user_id BIGINT NOT NULL,
    status INT NULL DEFAULT NULL, -- Ví dụ: đang học, đã hoàn thành, bảo lưu
    FOREIGN KEY (class_id) REFERENCES tbl_class(id),
    FOREIGN KEY (user_id) REFERENCES tbl_user(id),
    UNIQUE KEY unique_class_user (class_id, user_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;


-- Ca thi
DROP TABLE IF EXISTS tbl_exam_session;
CREATE TABLE tbl_exam_session (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	created_by VARCHAR(255) NOT NULL,
    create_date DATETIME NOT NULL,
    modified_by VARCHAR(255) NULL DEFAULT NULL,
    modify_date DATETIME NULL DEFAULT NULL,
    exam_id BIGINT NOT NULL,
    code VARCHAR(255) NULL DEFAULT NULL,
    class_id BIGINT NULL DEFAULT NULL,
    teacher_id BIGINT NULL DEFAULT NULL,
    session_date DATETIME NULL DEFAULT NULL,
    start_time DATETIME NULL DEFAULT NULL,
    end_time DATETIME NULL DEFAULT NULL,
    status INT NULL DEFAULT NULL,
    FOREIGN KEY (exam_id) REFERENCES tbl_exam(id),
    FOREIGN KEY (class_id) REFERENCES tbl_class(id),
    FOREIGN KEY (teacher_id) REFERENCES tbl_user(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;


-- Ca thi -sinh viên  
-- Bảng trung gian: Ca thi - Sinh viên
DROP TABLE IF EXISTS tbl_exam_session_student;
CREATE TABLE tbl_exam_session_student (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    exam_session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    start_time DATETIME NULL DEFAULT NULL,
    end_time DATETIME NULL DEFAULT NULL,
    submit_time DATETIME NULL DEFAULT NULL,
    status INT NULL DEFAULT NULL, -- Trạng thái riêng của từng sinh viên
    score DECIMAL(10,2) NULL DEFAULT NULL, -- Điểm số của sinh viên
    FOREIGN KEY (exam_session_id) REFERENCES tbl_exam_session(id),
    FOREIGN KEY (student_id) REFERENCES tbl_user(id),
    UNIQUE KEY unique_exam_session_student (exam_session_id, student_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;


-- Câu trả lời của học sinh
DROP TABLE IF EXISTS tbl_student_answers;
CREATE TABLE tbl_student_answers (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_by VARCHAR(255) NOT NULL,
    create_date DATETIME NOT NULL,
    modified_by VARCHAR(255) NULL DEFAULT NULL,
    modify_date DATETIME NULL DEFAULT NULL,
    exam_session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    answer_id BIGINT NULL DEFAULT NULL,
    is_correct BIT(1) NULL DEFAULT NULL,
    answer_date DATETIME NULL DEFAULT NULL,
    FOREIGN KEY (exam_session_id) REFERENCES tbl_exam_session(id),
    FOREIGN KEY (student_id) REFERENCES tbl_user(id),
    FOREIGN KEY (question_id) REFERENCES tbl_question(id),
    FOREIGN KEY (answer_id) REFERENCES tbl_answer(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Kết quả thi
DROP TABLE IF EXISTS tbl_exam_result;
CREATE TABLE tbl_exam_result (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_by VARCHAR(255) NOT NULL,
    create_date DATETIME NOT NULL,
    modified_by VARCHAR(255) NULL DEFAULT NULL,
    modify_date DATETIME NULL DEFAULT NULL,
    exam_session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    total_score DECIMAL(10,2) NULL DEFAULT NULL,
    correct_count INT NULL DEFAULT NULL,
    wrong_count INT NULL DEFAULT NULL,
    un_answer_count INT NULL DEFAULT NULL,
    FOREIGN KEY (exam_session_id) REFERENCES tbl_exam_session(id),
    FOREIGN KEY (student_id) REFERENCES tbl_user(id),
    UNIQUE KEY unique_exam_result (exam_session_id, student_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- File mô tả
DROP TABLE IF EXISTS file_description;
CREATE TABLE file_description (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    created_by VARCHAR(255) NOT NULL,
    create_date DATETIME NOT NULL,
    modified_by VARCHAR(255) NULL DEFAULT NULL,
    modify_date DATETIME NULL DEFAULT NULL,
    content_size INT NULL DEFAULT NULL,
    content_type VARCHAR(50) NULL DEFAULT NULL,
    name VARCHAR(255) NULL DEFAULT NULL,
	file_path VARCHAR(255) NULL DEFAULT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Kích hoạt lại kiểm tra khóa ngoại
SET foreign_key_checks = 1;
