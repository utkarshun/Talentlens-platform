import api from "../../api/axiosConfig";

export const loginUser = async (email: string, password: string) => {
  const res = await api.post("/api/auth/login", { email, password });
  return res.data;
};

export const registerUser = async (name: string, email: string, password: string) => {
  const res = await api.post("/api/auth/register", { name, email, password });
  return res.data;
};
