export interface AnswerRequest {
    questionId: number;
    answer: string;
}

export interface Question {
    id: number;
    questionText: string;
    options: string[];
}

export interface Exam {
    id: number;
    title: string;
    description: string;
    durationMinutes: number;
    category?: "LIVE" | "PRACTICE";
}

export interface QuestionReview {
    questionText: string;
    studentAnswer: string;
    correctAnswer: string;
    isCorrect: boolean;
    options: string[];
}

export interface AttemptReview {
    attemptId: number;
    studentName: string;
    examTitle: string;
    score: number;
    totalQuestions: number;
    questions: QuestionReview[];
    suspiciousLogs: string[];
}
