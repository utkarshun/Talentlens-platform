import api from "../../api/axiosConfig";
import { Exam } from "../../types/types";

export const generatePracticeContent = async () => {
    const res = await api.post("/api/practice/generate");
    return res.data;
};

export const getPracticeExams = async (): Promise<Exam[]> => {
    const res = await api.get("/api/practice/exams");
    return res.data;
};
