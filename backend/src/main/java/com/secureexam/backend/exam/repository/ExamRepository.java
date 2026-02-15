package com.secureexam.backend.exam.repository;

import com.secureexam.backend.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    java.util.Optional<Exam> findByTitle(String title);

}
