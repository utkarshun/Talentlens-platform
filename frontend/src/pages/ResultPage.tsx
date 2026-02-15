import { Box, Paper, Typography } from "@mui/material";

import { useLocation } from "react-router-dom";

export default function ResultPage() {
  const { state } = useLocation();
  if (!state) return <h3>No Result Available</h3>;
  return (
    <Box sx={{ p: 4 }}>
      <Paper sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom>
          Exam Result
        </Typography>
        <Typography>Score:{state.score}</Typography>
        <Typography>Suspicious Score: {state.suspiciousScore}</Typography>
        <Typography>Status:{state.flagged ? "Flagged" : "Clean"}</Typography>
      </Paper>
    </Box>
  );
}
