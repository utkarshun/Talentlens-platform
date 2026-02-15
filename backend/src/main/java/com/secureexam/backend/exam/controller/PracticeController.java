package com.secureexam.backend.exam.controller;

import com.secureexam.backend.exam.entity.Exam;
import com.secureexam.backend.exam.service.PracticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {

    private final PracticeService practiceService;

    public PracticeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> generatePracticeContent() {
        new Thread(practiceService::generatePracticeContent).start(); // Run async
        return ResponseEntity.ok("Practice content generation started. This may take a few minutes.");
    }

    @GetMapping("/exams")
    public List<Exam> getPracticeExams() {
        return practiceService.getPracticeExams();
    }
}
