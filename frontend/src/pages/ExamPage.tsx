import { submitAttempt } from "../features/attempt/attemptApi";
import { logSecurityEvent } from "../features/security/securityApi";
import { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useAuthStore } from "../store/authStore";

import {
  Box,
  AppBar,
  Toolbar,
  Typography,
  Button,
  Grid,
  Paper,
  RadioGroup,
  FormControlLabel,
  Radio,
  Container,
  Card,
  CardContent,
  CardActions,
  Divider,
  Chip,
} from "@mui/material";
import TimerIcon from "@mui/icons-material/Timer";

import { AnswerRequest } from "../types/types";

interface Question {
  id: number;
  questionText: string;
  options: string[];
}

export default function ExamPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuthStore();

  const attemptId = Number(localStorage.getItem("attemptId") || 0);

  const state = location.state as any;
  const duration = state?.duration ?? 60;
  const [timeLeft, setTimeLeft] = useState(duration * 60);
  const [questions] = useState<Question[]>(
    state?.questions || []
  );

  const [answers, setAnswers] = useState<{ [key: number]: string }>({});
  const [currentIndex, setCurrentIndex] = useState(0);

  const handleOptionSelect = (questionId: number, option: string) => {
    setAnswers((prev) => ({ ...prev, [questionId]: option }));
  };

  const handleSubmitExam = async () => {
    try {
      if (!user?.id) return;

      const formattedAnswers: AnswerRequest[] = Object.entries(answers).map(
        ([qId, opt]) => ({
          questionId: Number(qId),
          answer: opt,
        })
      );

      const res = await submitAttempt(Number(id), user.id, formattedAnswers);
      navigate("/result", { state: res });
    } catch (err: any) {
      const msg = err.response?.data?.message || "Submission Failed";
      alert(msg);
    }
  };

  /* TIMER */
  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timer);
          handleSubmitExam();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [handleSubmitExam]);

  /* TAB SWITCH LOGGING */
  useEffect(() => {
    const handleVisibility = () => {
      if (document.hidden && attemptId)
        logSecurityEvent(attemptId, "TAB_SWITCH");
    };

    document.addEventListener("visibilitychange", handleVisibility);
    return () =>
      document.removeEventListener("visibilitychange", handleVisibility);
  }, [attemptId]);

  /* FORCE FULLSCREEN */
  useEffect(() => {
    document.documentElement.requestFullscreen().catch(() => { });
  }, []);

  const formatTime = (seconds: number) => {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m.toString().padStart(2, "0")}:${s.toString().padStart(2, "0")}`;
  };

  return (
    <Box sx={{ flexGrow: 1, minHeight: "100vh", bgcolor: "#f4f6f8" }}>
      <AppBar position="sticky" elevation={2}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: "bold" }}>
            Secure Exam Environment
          </Typography>

          <Box display="flex" alignItems="center" mr={3}>
            <TimerIcon sx={{ mr: 1, color: timeLeft < 300 ? "error.light" : "inherit" }} />
            <Typography
              variant="h5"
              sx={{
                color: timeLeft < 300 ? "error.light" : "inherit",
                fontWeight: "bold",
                fontFamily: "monospace",
              }}
            >
              {formatTime(timeLeft)}
            </Typography>
          </Box>

          <Button
            variant="contained"
            color="secondary"
            onClick={handleSubmitExam}
            sx={{ fontWeight: "bold" }}
          >
            Submit Exam
          </Button>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Grid container spacing={3}>
          {/* QUESTION PANEL */}
          <Grid size={{ xs: 12, md: 8 }}>
            <Card elevation={4} sx={{ borderRadius: 3, minHeight: "60vh", display: "flex", flexDirection: "column" }}>
              {questions.length > 0 ? (
                <>
                  <CardContent sx={{ flexGrow: 1, p: 4 }}>
                    <Box display="flex" justifyContent="space-between" mb={2}>
                      <Chip label={`Question ${currentIndex + 1} of ${questions.length}`} color="primary" variant="outlined" />
                    </Box>

                    <Typography variant="h5" gutterBottom sx={{ fontWeight: 500, mb: 4 }}>
                      {questions[currentIndex]?.questionText}
                    </Typography>

                    <Divider sx={{ mb: 3 }} />

                    <RadioGroup
                      value={answers[questions[currentIndex]?.id] || ""}
                      onChange={(e) =>
                        handleOptionSelect(
                          questions[currentIndex].id,
                          e.target.value
                        )
                      }
                    >
                      <Grid container spacing={2}>
                        {questions[currentIndex]?.options.map((opt, idx) => (
                          <Grid size={{ xs: 12 }} key={idx}>
                            <Paper
                              variant="outlined"
                              sx={{
                                p: 1,
                                pl: 2,
                                borderColor: answers[questions[currentIndex].id] === opt ? 'primary.main' : 'divider',
                                bgcolor: answers[questions[currentIndex].id] === opt ? 'primary.50' : 'background.paper',
                                '&:hover': { bgcolor: 'action.hover' }
                              }}
                            >
                              <FormControlLabel
                                value={opt}
                                control={<Radio />}
                                label={<Typography variant="body1">{opt}</Typography>}
                                sx={{ width: '100%', m: 0 }}
                              />
                            </Paper>
                          </Grid>
                        ))}
                      </Grid>
                    </RadioGroup>
                  </CardContent>

                  <CardActions sx={{ p: 3, pt: 0, justifyContent: "space-between" }}>
                    <Button
                      variant="outlined"
                      disabled={currentIndex === 0}
                      onClick={() => setCurrentIndex((prev) => prev - 1)}
                      size="large"
                    >
                      Previous
                    </Button>

                    {currentIndex === questions.length - 1 ? (
                      <Button
                        variant="contained"
                        color="secondary"
                        onClick={handleSubmitExam}
                        size="large"
                        sx={{ fontWeight: "bold" }}
                      >
                        Submit Exam
                      </Button>
                    ) : (
                      <Button
                        variant="contained"
                        onClick={() => setCurrentIndex((prev) => prev + 1)}
                        size="large"
                      >
                        Next
                      </Button>
                    )}
                  </CardActions>
                </>
              ) : (
                <Box p={4} textAlign="center">
                  <Typography>Loading questions...</Typography>
                </Box>
              )}
            </Card>
          </Grid>

          {/* QUESTION PALETTE */}
          <Grid size={{ xs: 12, md: 4 }}>
            <Card elevation={3} sx={{ borderRadius: 3 }}>
              <CardContent>
                <Typography variant="h6" gutterBottom sx={{ mb: 3 }}>
                  Question Palette
                </Typography>
                <Divider sx={{ mb: 3 }} />

                <Grid container spacing={1}>
                  {questions.map((q, idx) => {
                    const isAnswered = !!answers[q.id];
                    const isCurrent = idx === currentIndex;

                    return (
                      <Grid key={q.id} size="auto">
                        <Button
                          variant={isCurrent ? "contained" : isAnswered ? "outlined" : "text"}
                          color={isAnswered ? "success" : "primary"}
                          onClick={() => setCurrentIndex(idx)}
                          sx={{
                            minWidth: 40,
                            height: 40,
                            borderRadius: '50%',
                            fontWeight: 'bold',
                            border: isAnswered && !isCurrent ? '1px solid' : undefined,
                            borderColor: isAnswered ? 'success.main' : undefined,
                            bgcolor: isAnswered && !isCurrent ? 'success.light' : undefined,
                            color: isAnswered && !isCurrent ? 'success.dark' : undefined
                          }}
                        >
                          {idx + 1}
                        </Button>
                      </Grid>
                    );
                  })}
                </Grid>

                <Box mt={4} display="flex" gap={2} flexWrap="wrap">
                  <Box display="flex" alignItems="center" gap={1}>
                    <Box width={16} height={16} bgcolor="primary.main" borderRadius="50%" />
                    <Typography variant="caption">Current</Typography>
                  </Box>
                  <Box display="flex" alignItems="center" gap={1}>
                    <Box width={16} height={16} bgcolor="success.light" border="1px solid" borderColor="success.main" borderRadius="50%" />
                    <Typography variant="caption">Answered</Typography>
                  </Box>
                  <Box display="flex" alignItems="center" gap={1}>
                    <Box width={16} height={16} bgcolor="transparent" border="1px solid gray" borderRadius="50%" />
                    <Typography variant="caption">Not Visited</Typography>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
}