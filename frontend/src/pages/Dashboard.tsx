import { useEffect, useState } from "react";
import { fetchExams } from "../features/exam/examApi";
import { getPracticeExams } from "../features/practice/practiceApi";
import { useNavigate } from "react-router-dom";
import { Exam } from "../types/types";
import { startExamAttempt } from "../features/attempt/attemptApi";
import { useAuthStore } from "../store/authStore";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Container,
  Card,
  CardContent,
  CardActions,
  Box,
  CircularProgress,
  Chip,
} from "@mui/material";

export default function Dashboard() {
  const [exams, setExams] = useState<Exam[]>([]);
  const [practiceExams, setPracticeExams] = useState<Exam[]>([]);
  const [loadingId, setLoadingId] = useState<number | null>(null);
  const navigate = useNavigate();
  const { user, logout } = useAuthStore();

  useEffect(() => {
    const loadExams = async () => {
      try {
        const liveData = await fetchExams();
        // Filter out practice exams from the main list if the backend returns them mixed, 
        // OR just rely on the separate endpoint. 
        // Assuming fetchExams returns ALL, we might need to filter. 
        // But for now, let's assume fetchExams returns non-practice ones or we filter here.
        // Actually, let's filter just in case fetchExams returns everything.
        setExams(liveData.filter((e: Exam) => e.category !== "PRACTICE"));

        const practiceData = await getPracticeExams();
        setPracticeExams(practiceData);
      } catch (err) {
        console.error(err);
      }
    };

    loadExams();
  }, []);

  const handleStartExam = async (examId: number) => {
    if (!user?.id) {
      alert("User not found. Please log in.");
      return;
    }
    try {
      setLoadingId(examId);
      const data = await startExamAttempt(examId, user.id);
      localStorage.setItem("attemptId", data.attemptId.toString());
      navigate(`/exam/${examId}`, { state: data });
    } catch (err) {
      console.error(err);
      alert("Unable to start exam. Please try again.");
    } finally {
      setLoadingId(null);
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const renderExamCard = (exam: Exam, isPractice: boolean = false) => (
    <Box
      key={exam.id}
      sx={{
        flex: { xs: "1 1 100%", sm: "1 1 45%", md: "1 1 30%" },
        minWidth: 280,
      }}
    >
      <Card elevation={3} sx={{ borderRadius: 2, height: "100%", display: 'flex', flexDirection: 'column' }}>
        <CardContent sx={{ flexGrow: 1 }}>
          <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", mb: 1 }}>
            <Typography variant="h5" component="div">
              {exam.title}
            </Typography>
            {isPractice && <Chip label="Practice" color="success" size="small" variant="outlined" />}
          </Box>
          <Typography color="text.secondary" gutterBottom>
            {isPractice ? "Untimed • Instant Feedback" : `Duration: ${exam.durationMinutes} minutes`}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {exam.description || (isPractice ? "Practice your skills with AI-generated questions." : "Live exam session.")}
          </Typography>
        </CardContent>
        <CardActions sx={{ p: 2, pt: 0 }}>
          <Button
            size="large"
            variant={isPractice ? "outlined" : "contained"}
            color={isPractice ? "success" : "primary"}
            fullWidth
            onClick={() => handleStartExam(exam.id)}
            disabled={loadingId === exam.id}
            startIcon={
              loadingId === exam.id ? (
                <CircularProgress size={20} color="inherit" />
              ) : null
            }
          >
            {loadingId === exam.id ? "Starting..." : isPractice ? "Start Practice" : "Start Exam"}
          </Button>
        </CardActions>
      </Card>
    </Box>
  );

  return (
    <Box sx={{ flexGrow: 1, minHeight: "100vh", bgcolor: "#f5f5f5" }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Secure Exam Dashboard
          </Typography>
          <Typography variant="subtitle1" sx={{ mr: 2 }}>
            Welcome, {user?.name || "Student"}
          </Typography>
          <Button color="inherit" onClick={handleLogout}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ mt: 4, pb: 8 }}>
        {/* Live Exams Section */}
        <Typography variant="h4" gutterBottom color="textSecondary" sx={{ mb: 3 }}>
          Available Exams
        </Typography>
        {exams.length === 0 ? (
          <Typography sx={{ mb: 6 }} color="text.secondary">No live exams available at the moment.</Typography>
        ) : (
          <Box sx={{ display: "flex", flexWrap: "wrap", gap: 3, mb: 6 }}>
            {exams.map((exam) => renderExamCard(exam))}
          </Box>
        )}

        {/* Practice Section */}
        <Box sx={{ borderTop: 1, borderColor: 'divider', pt: 4 }}>
          <Typography variant="h4" gutterBottom color="textSecondary" sx={{ mb: 1 }}>
            Practice Interviews
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
            Prepare for your SDE roles with our AI-powered practice sets.
          </Typography>

          {practiceExams.length === 0 ? (
            <Typography>No practice sets generated yet. Ask an admin to generate them.</Typography>
          ) : (
            <Box sx={{ display: "flex", flexWrap: "wrap", gap: 3 }}>
              {practiceExams.map((exam) => renderExamCard(exam, true))}
            </Box>
          )}
        </Box>
      </Container>
    </Box>
  );
}
