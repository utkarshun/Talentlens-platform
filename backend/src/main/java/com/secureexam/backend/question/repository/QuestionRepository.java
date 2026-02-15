package com.secureexam.backend.question.repository;

import com.secureexam.backend.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByExamId(Long examId);

    @Query(value = """
            SELECT * FROM questions
            WHERE exam_id = :examId
            ORDER BY RANDOM()
            LIMIT :limit
            """, nativeQuery = true)
    List<Question> findRandomByExamId(
            @Param("examId") Long examId,
            @Param("limit") int limit
    );
}