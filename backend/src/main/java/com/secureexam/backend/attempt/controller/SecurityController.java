package com.secureexam.backend.attempt.controller;

import com.secureexam.backend.attempt.entity.ExamAttempt;
import com.secureexam.backend.attempt.repository.ExamAttemptRepository;
import com.secureexam.backend.security.dto.SecurityLogRequest;
import com.secureexam.backend.security.entity.SuspiciousActivity;
import com.secureexam.backend.security.repository.SuspiciousActivityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final SuspiciousActivityRepository repo;
    private final ExamAttemptRepository attemptRepo;

    public SecurityController(
            SuspiciousActivityRepository repo,
            ExamAttemptRepository attemptRepo) {
        this.repo = repo;
        this.attemptRepo = attemptRepo;
    }

    @PostMapping("/log")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<String> logActivity(
            @RequestBody SecurityLogRequest req,
            HttpServletRequest request) {

        // 0️⃣ Validate Attempt
        ExamAttempt attempt = attemptRepo.findById(req.attemptId())
                .orElseThrow(() -> new RuntimeException("Exam attempt not found"));

        if (attempt.getSubmittedAt() != null) {
            return ResponseEntity.badRequest().body("Exam already submitted");
        }

        // 1️⃣ Save suspicious activity
        SuspiciousActivity log = new SuspiciousActivity();
        log.setUserId(attempt.getStudent().getId());
        log.setExamId(attempt.getExam().getId());
        log.setActivityType(req.activityType().name());
        log.setDetails(req.details());
        log.setTimestamp(LocalDateTime.now());
        log.setIpAddress(request.getRemoteAddr());

        repo.save(log);

        // 2️⃣ Calculate suspicious score
        int score = calculateScore(req.activityType().name());

        // 3️⃣ Update exam attempt score + auto flag
        int newScore = attempt.getSuspiciousScore() + score;
        attempt.setSuspiciousScore(newScore);

        // Auto cheating flag
        if (newScore > 10) {
            attempt.setFlagged(true);
        }

        attemptRepo.save(attempt);

        return ResponseEntity.ok("Suspicious activity logged successfully.");
    }

    private int calculateScore(String type) {
        return switch (type) {
            case "TAB_SWITCH" -> 2;
            case "FULLSCREEN_EXIT" -> 3;
            case "COPY_PASTE" -> 4;
            case "MULTIPLE_LOGIN" -> 5;
            case "LATE_SUBMISSION" -> 6;
            default -> 1;
        };
    }
}