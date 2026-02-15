import { useEffect, useState } from "react";
import {
  Box,
  Paper,
  Typography,
  CircularProgress,
  Button,
  Snackbar,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip,
  Card,
  CardContent,
  IconButton,
  Tooltip,
  Container,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import { generatePracticeContent } from "../features/practice/practiceApi";
import RefreshIcon from "@mui/icons-material/Refresh";
import AutoFixHighIcon from "@mui/icons-material/AutoFixHigh";
import VisibilityIcon from "@mui/icons-material/Visibility";
import WarningIcon from "@mui/icons-material/Warning";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import AssessmentIcon from "@mui/icons-material/Assessment";
import PeopleIcon from "@mui/icons-material/People";
import FlagIcon from "@mui/icons-material/Flag";

interface Attempt {
  id: number;
  student?: {
    email: string;
  };
  exam?: {
    title: string;
  };
  score: number;
  suspiciousScore: number;
  flagged: boolean;
}

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [attempts, setAttempts] = useState<Attempt[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [generating, setGenerating] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, message: "" });

  const fetchAttempts = async () => {
    setLoading(true);
    try {
      const res = await api.get("/api/attempt/all");
      if (Array.isArray(res.data)) {
        setAttempts(res.data);
        setError("");
      } else {
        console.error("API returned non-array data:", res.data);
        setError("Received invalid data from server.");
      }
    } catch (err) {
      console.error(err);
      setError("Failed to load attempts. Ensure you are logged in as Admin.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAttempts();
  }, []);

  const handleGeneratePractice = async () => {
    if (!confirm("This will generate ~50 questions for 5 topics using OpenAI. It may take some time. Continue?")) return;

    setGenerating(true);
    try {
      await generatePracticeContent();
      setSnackbar({ open: true, message: "Practice content generation started in the background!" });
    } catch (err) {
      console.error(err);
      setSnackbar({ open: true, message: "Failed to start generation." });
    } finally {
      setGenerating(false);
    }
  };

  // Stats Calculation
  const totalAttempts = attempts.length;
  const flaggedAttempts = attempts.filter((a) => a.flagged).length;
  const avgScore = totalAttempts > 0 ? (attempts.reduce((sum, a) => sum + a.score, 0) / totalAttempts).toFixed(1) : "0";

  if (loading && attempts.length === 0) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 10, height: "100vh" }}>
        <CircularProgress size={60} />
      </Box>
    );
  }

  if (error) {
    return (
      <Container maxWidth="md" sx={{ mt: 8, textAlign: "center" }}>
        <Typography variant="h5" color="error" gutterBottom>
          {error}
        </Typography>
        <Button variant="outlined" onClick={fetchAttempts} startIcon={<RefreshIcon />}>
          Retry
        </Button>
      </Container>
    );
  }

  return (
    <Box sx={{ flexGrow: 1, p: 3, backgroundColor: "#f5f7fa", minHeight: "100vh" }}>
      <Container maxWidth="xl">
        {/* Header Section */}
        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 4 }}>
          <Box>
            <Typography variant="h4" fontWeight="bold" color="text.primary">
              Admin Dashboard
            </Typography>
            <Typography variant="subtitle1" color="text.secondary">
              Monitor exam attempts and manage content
            </Typography>
          </Box>
          <Box sx={{ display: "flex", gap: 2 }}>
            <Button
              variant="outlined"
              onClick={fetchAttempts}
              startIcon={<RefreshIcon />}
              disabled={loading}
            >
              Refresh
            </Button>
            <Button
              variant="contained"
              color="primary"
              onClick={handleGeneratePractice}
              disabled={generating}
              startIcon={generating ? <CircularProgress size={20} color="inherit" /> : <AutoFixHighIcon />}
              sx={{ boxShadow: 2 }}
            >
              {generating ? "Generating..." : "Generate Practice Content"}
            </Button>
          </Box>
        </Box>

        {/* Stats Cards */}
        <Box sx={{ display: "flex", flexWrap: "wrap", gap: 3, mb: 4 }}>
          <Box sx={{ flex: { xs: "1 1 100%", sm: "1 1 30%" } }}>
            <Card sx={{ borderRadius: 3, boxShadow: 3, height: "100%" }}>
              <CardContent sx={{ display: "flex", alignItems: "center", p: 3 }}>
                <AvatarWrapper bgcolor="#e3f2fd" color="#1976d2">
                  <AssessmentIcon fontSize="large" />
                </AvatarWrapper>
                <Box sx={{ ml: 2 }}>
                  <Typography variant="h4" fontWeight="bold">
                    {totalAttempts}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Total Attempts
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Box>
          <Box sx={{ flex: { xs: "1 1 100%", sm: "1 1 30%" } }}>
            <Card sx={{ borderRadius: 3, boxShadow: 3, height: "100%" }}>
              <CardContent sx={{ display: "flex", alignItems: "center", p: 3 }}>
                <AvatarWrapper bgcolor="#e8f5e9" color="#2e7d32">
                  <CheckCircleIcon fontSize="large" />
                </AvatarWrapper>
                <Box sx={{ ml: 2 }}>
                  <Typography variant="h4" fontWeight="bold">
                    {avgScore}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Average Score
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Box>
          <Box sx={{ flex: { xs: "1 1 100%", sm: "1 1 30%" } }}>
            <Card sx={{ borderRadius: 3, boxShadow: 3, height: "100%" }}>
              <CardContent sx={{ display: "flex", alignItems: "center", p: 3 }}>
                <AvatarWrapper bgcolor="#ffebee" color="#d32f2f">
                  <FlagIcon fontSize="large" />
                </AvatarWrapper>
                <Box sx={{ ml: 2 }}>
                  <Typography variant="h4" fontWeight="bold" color="error">
                    {flaggedAttempts}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Flagged Attempts
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Box>
        </Box>
        {/* Attempts Table */}
        <Paper sx={{ borderRadius: 3, boxShadow: 3, overflow: "hidden" }}>
          <Box sx={{ p: 3, borderBottom: "1px solid #eee" }}>
            <Typography variant="h6" fontWeight="bold">
              Recent Exam Attempts
            </Typography>
          </Box>
          <TableContainer>
            <Table>
              <TableHead sx={{ backgroundColor: "#f9fafb" }}>
                <TableRow>
                  <TableCell><strong>Student</strong></TableCell>
                  <TableCell><strong>Exam Title</strong></TableCell>
                  <TableCell align="center"><strong>Score</strong></TableCell>
                  <TableCell align="center"><strong>Suspicious Score</strong></TableCell>
                  <TableCell align="center"><strong>Status</strong></TableCell>
                  <TableCell align="center"><strong>Actions</strong></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {attempts.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={6} align="center" sx={{ py: 4 }}>
                      <Typography color="text.secondary">No attempts found.</Typography>
                    </TableCell>
                  </TableRow>
                ) : (
                  attempts.map((a) => (
                    <TableRow key={a.id} hover>
                      <TableCell>
                        <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                          <AvatarWrapper bgcolor="#eeeeee" color="#616161" size={32}>
                            <PeopleIcon fontSize="small" />
                          </AvatarWrapper>
                          <Typography variant="body2" fontWeight="medium">
                            {a.student?.email || "Unknown"}
                          </Typography>
                        </Box>
                      </TableCell>
                      <TableCell>{a.exam?.title || "Unknown"}</TableCell>
                      <TableCell align="center">
                        <Chip label={a.score} size="small" variant="outlined" />
                      </TableCell>
                      <TableCell align="center">
                        <Chip
                          label={a.suspiciousScore}
                          size="small"
                          color={a.suspiciousScore > 50 ? "error" : "default"}
                          variant={a.suspiciousScore > 50 ? "filled" : "outlined"}
                        />
                      </TableCell>
                      <TableCell align="center">
                        {a.flagged ? (
                          <Chip icon={<WarningIcon />} label="Flagged" color="error" size="small" />
                        ) : (
                          <Chip icon={<CheckCircleIcon />} label="Clean" color="success" size="small" />
                        )}
                      </TableCell>
                      <TableCell align="center">
                        <Tooltip title="View Details">
                          <IconButton
                            color="primary"
                            size="small"
                            onClick={() => navigate(`/attempt-review/${a.id}`)}
                          >
                            <VisibilityIcon />
                          </IconButton>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>

        <Snackbar
          open={snackbar.open}
          autoHideDuration={6000}
          onClose={() => setSnackbar({ ...snackbar, open: false })}
        >
          <Alert severity="success" sx={{ width: "100%" }}>
            {snackbar.message}
          </Alert>
        </Snackbar>
      </Container>
    </Box>
  );
}

// Helper component for Icons with background
function AvatarWrapper({
  children,
  bgcolor,
  color,
  size = 56,
}: {
  children: React.ReactNode;
  bgcolor: string;
  color: string;
  size?: number;
}) {
  return (
    <Box
      sx={{
        width: size,
        height: size,
        borderRadius: "50%",
        backgroundColor: bgcolor,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        color: color,
      }}
    >
      {children}
    </Box>
  );
}
