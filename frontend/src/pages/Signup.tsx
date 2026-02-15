import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
    TextField,
    Button,
    Paper,
    Typography,
    Box,
    InputAdornment,
    Container,
    CssBaseline,
    Avatar,
    Link,
} from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import EmailIcon from "@mui/icons-material/Email";
import LockIcon from "@mui/icons-material/Lock";
import PersonIcon from "@mui/icons-material/Person";
import { registerUser } from "../features/auth/authApi";
import { useAuthStore } from "../store/authStore";
import { toast } from "react-toastify";

export default function Signup() {
    const navigate = useNavigate();
    const setAuth = useAuthStore((state) => state.setAuth);

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSignup = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        try {
            const data = await registerUser(name, email, password);
            setAuth(data.token, data.user);

            toast.success("Account created successfully");
            navigate("/dashboard");
        } catch (err: any) {
            console.error(err);
            toast.error(err?.response?.data?.message || "Signup failed");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <Box
                sx={{
                    marginTop: 8,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                }}
            >
                <Paper
                    elevation={10}
                    sx={{
                        display: "flex",
                        borderRadius: 4,
                        overflow: "hidden",
                        width: "100%",
                        maxWidth: 900,
                        minHeight: 500,
                    }}
                >
                    {/* Left Side - Image/Branding */}
                    <Box sx={{ display: "flex", width: "100%", height: "100%" }}>
                        <Box
                            sx={{
                                width: { xs: "0", sm: "33.33%", md: "50%" },
                                backgroundImage: "url(https://source.unsplash.com/random?learning,technology)",
                                backgroundRepeat: "no-repeat",
                                backgroundColor: (t) =>
                                    t.palette.mode === "light"
                                        ? t.palette.grey[50]
                                        : t.palette.grey[900],
                                backgroundSize: "cover",
                                backgroundPosition: "center",
                                display: { xs: "none", sm: "block" },
                            }}
                        />
                        {/* Right Side - Form */}
                        <Box
                            sx={{
                                width: { xs: "100%", sm: "66.67%", md: "50%" },
                                display: "flex",
                                flexDirection: "column",
                                alignItems: "center",
                                justifyContent: "center",
                                p: 4,
                            }}
                        >
                            <Avatar sx={{ m: 1, bgcolor: "secondary.main", width: 56, height: 56 }}>
                                <LockOutlinedIcon fontSize="large" />
                            </Avatar>
                            <Typography component="h1" variant="h4" fontWeight="bold" gutterBottom>
                                Create Account
                            </Typography>
                            <Typography variant="body2" color="textSecondary" mb={3}>
                                Join Secure Exam Platform today
                            </Typography>
                            <Box component="form" onSubmit={handleSignup} noValidate sx={{ mt: 1, width: "100%" }}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="name"
                                    label="Full Name"
                                    name="name"
                                    autoComplete="name"
                                    autoFocus
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <PersonIcon color="action" />
                                            </InputAdornment>
                                        ),
                                    }}
                                    sx={{ mb: 2 }}
                                />
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="email"
                                    label="Email Address"
                                    name="email"
                                    autoComplete="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <EmailIcon color="action" />
                                            </InputAdornment>
                                        ),
                                    }}
                                    sx={{ mb: 2 }}
                                />
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    name="password"
                                    label="Password"
                                    type="password"
                                    id="password"
                                    autoComplete="new-password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <LockIcon color="action" />
                                            </InputAdornment>
                                        ),
                                    }}
                                    sx={{ mb: 3 }}
                                />
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    size="large"
                                    disabled={loading}
                                    sx={{
                                        mt: 1,
                                        mb: 2,
                                        py: 1.5,
                                        fontSize: "1rem",
                                        fontWeight: "bold",
                                        borderRadius: 2,
                                        textTransform: "none",
                                    }}
                                >
                                    {loading ? "Creating Account..." : "Sign Up"}
                                </Button>
                                <Box sx={{ display: "flex", justifyContent: "center" }}>
                                    <Link href="/" variant="body2" underline="hover">
                                        {"Already have an account? Sign In"}
                                    </Link>
                                </Box>
                            </Box>
                        </Box>
                    </Box>
                </Paper>
            </Box>
        </Container>
    );
}
