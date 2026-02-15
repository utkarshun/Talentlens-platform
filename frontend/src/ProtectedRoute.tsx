import { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuthStore } from "./store/authStore";

interface ProtectedRouteProps {
    children: ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
    const token = useAuthStore((state: any) => state.token);

    if (!token) {
        return <Navigate to="/" replace />;
    }

    return <>{children}</>;
};

export default ProtectedRoute;
