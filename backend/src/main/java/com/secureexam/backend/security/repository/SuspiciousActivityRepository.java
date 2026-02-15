package com.secureexam.backend.security.repository;

import com.secureexam.backend.security.entity.SuspiciousActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuspiciousActivityRepository extends JpaRepository<SuspiciousActivity, Long> {
    java.util.List<SuspiciousActivity> findByUserIdAndExamId(Long userId, Long examId);
}
