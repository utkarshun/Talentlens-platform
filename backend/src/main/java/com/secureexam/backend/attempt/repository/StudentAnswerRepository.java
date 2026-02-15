package com.secureexam.backend.attempt.repository;

import com.secureexam.backend.attempt.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    java.util.List<StudentAnswer> findByExamAttempt(com.secureexam.backend.attempt.entity.ExamAttempt examAttempt);
}
