package com.secureexam.backend.attempt.repository;

import com.secureexam.backend.attempt.entity.ExamAttempt;
import com.secureexam.backend.exam.entity.Exam;
import com.secureexam.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    boolean existsByExamAndStudent(Exam exam, User student);

    java.util.Optional<ExamAttempt> findByExamAndStudent(Exam exam, User student);

}
