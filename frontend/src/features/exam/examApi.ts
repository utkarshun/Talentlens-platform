import api from "../../api/axiosConfig";

export const fetchExams = async () => {
  const res = await api.get("/api/exams");
  return res.data;
};

export const fetchExamQuestions = async (examId: number) => {
  const res = await api.get(`/api/questions/exam/${examId}`);
  return res.data;
};
