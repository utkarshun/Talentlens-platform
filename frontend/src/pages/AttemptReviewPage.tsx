import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { getAttemptReview } from "../features/attempt/attemptApi";
import { AttemptReview } from "../types/types";

import {
    Box,
    Typography,
    Button,
    Paper,
    Container,
    Grid,
    Chip,
    Divider,
    CircularProgress,
    Alert,
    AlertTitle,
} from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CancelIcon from "@mui/icons-material/Cancel";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

const AttemptReviewPage = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [review, setReview] = useState<AttemptReview | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchReview = async () => {
            try {
                if (!id) return;
                const data = await getAttemptReview(id);
                setReview(data);
            } catch (error) {
                console.error(error);
                toast.error("Error loading attempt review");
            } finally {
                setLoading(false);
            }
        };

        fetchReview();
    }, [id, navigate]);

    if (loading)
        return (
            <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                minHeight="100vh"
            >
                <CircularProgress />
                <Typography variant="h6" ml={2}>
                    Loading review...
                </Typography>
            </Box>
        );

    if (!review)
        return (
            <Box p={4} textAlign="center">
                <Typography variant="h5" color="error">
                    Review not found
                </Typography>
                <Button onClick={() => navigate("/admin")} sx={{ mt: 2 }}>
                    Back to Dashboard
                </Button>
            </Box>
        );

    return (
        <Box sx={{ minHeight: "100vh", bgcolor: "#f4f6f8", py: 4 }}>
            <Container maxWidth="md">
                <Button
                    startIcon={<ArrowBackIcon />}
                    onClick={() => navigate("/admin")}
                    sx={{ mb: 3 }}
                >
                    Back to Dashboard
                </Button>

                <Paper elevation={3} sx={{ p: 4, mb: 4, borderRadius: 2 }}>
                    <Typography variant="h4" gutterBottom fontWeight="bold" color="primary">
                        Attempt Review
                    </Typography>
                    <Divider sx={{ mb: 3 }} />

                    <Grid container spacing={3}>
                        <Grid size={{ xs: 12, sm: 4 }}>
                            <Typography variant="subtitle2" color="textSecondary">
                                Student
                            </Typography>
                            <Typography variant="h6">{review.studentName}</Typography>
                        </Grid>
                        <Grid size={{ xs: 12, sm: 4 }}>
                            <Typography variant="subtitle2" color="textSecondary">
                                Exam
                            </Typography>
                            <Typography variant="h6">{review.examTitle}</Typography>
                        </Grid>
                        <Grid size={{ xs: 12, sm: 4 }}>
                            <Typography variant="subtitle2" color="textSecondary">
                                Score
                            </Typography>
                            <Typography
                                variant="h6"
                                color="primary"
                                sx={{ fontWeight: "bold" }}
                            >
                                {review.score} / {review.totalQuestions}
                            </Typography>
                        </Grid>
                    </Grid>
                </Paper>

                {review.suspiciousLogs.length > 0 && (
                    <Alert severity="warning" sx={{ mb: 4 }}>
                        <AlertTitle>Suspicious Activity Detected</AlertTitle>
                        <ul style={{ margin: 0, paddingLeft: 20 }}>
                            {review.suspiciousLogs.map((log, idx) => (
                                <li key={idx}>
                                    <Typography variant="body2">{log}</Typography>
                                </li>
                            ))}
                        </ul>
                    </Alert>
                )}

                <Typography variant="h5" gutterBottom sx={{ mb: 2 }}>
                    Question Breakdown
                </Typography>

                <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
                    {review.questions.map((q, index) => (
                        <Paper
                            key={index}
                            elevation={2}
                            sx={{
                                p: 3,
                                borderRadius: 2,
                                borderLeft: 6,
                                borderColor: q.isCorrect ? "success.main" : "error.main",
                            }}
                        >
                            <Box
                                display="flex"
                                justifyContent="space-between"
                                alignItems="start"
                                mb={2}
                            >
                                <Typography variant="h6" sx={{ fontWeight: 500 }}>
                                    Q{index + 1}: {q.questionText}
                                </Typography>
                                <Chip
                                    icon={q.isCorrect ? <CheckCircleIcon /> : <CancelIcon />}
                                    label={q.isCorrect ? "Correct" : "Incorrect"}
                                    color={q.isCorrect ? "success" : "error"}
                                    variant="outlined"
                                />
                            </Box>

                            <Box>
                                <Typography variant="body1" gutterBottom>
                                    <strong>Student Answer:</strong>{" "}
                                    <Typography
                                        component="span"
                                        color={q.isCorrect ? "success.main" : "error.main"}
                                        fontWeight="bold"
                                    >
                                        {q.studentAnswer}
                                    </Typography>
                                </Typography>

                                {!q.isCorrect && (
                                    <Typography variant="body1">
                                        <strong>Correct Answer:</strong>{" "}
                                        <Typography
                                            component="span"
                                            color="success.main"
                                            fontWeight="bold"
                                        >
                                            {q.correctAnswer}
                                        </Typography>
                                    </Typography>
                                )}
                            </Box>
                        </Paper>
                    ))}
                </Box>
            </Container>
        </Box >
    );
};

export default AttemptReviewPage;
