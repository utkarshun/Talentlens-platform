import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "../pages/Login";
import Signup from "../pages/Signup";
import Dashboard from "../pages/Dashboard";
import ExamPage from "../pages/ExamPage";
import ProtectedRoute from "../ProtectedRoute";
import ResultPage from "../pages/ResultPage";
import AdminDashboard from "../pages/AdminDashboard";
import AttemptReviewPage from "../pages/AttemptReviewPage";

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Signup />} />

        {/* User Dashboard */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />

        {/* Exam */}
        <Route
          path="/exam/:id"
          element={
            <ProtectedRoute>
              <ExamPage />
            </ProtectedRoute>
          }
        />

        {/* Admin Dashboard */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        {/* Attempt Review */}
        <Route
          path="/attempt-review/:id"
          element={
            <ProtectedRoute>
              <AttemptReviewPage />
            </ProtectedRoute>
          }
        />

        {/* Result Page */}
        <Route
          path="/result"
          element={
            <ProtectedRoute>
              <ResultPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;