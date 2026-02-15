import api from "../../api/axiosConfig";
import { AnswerRequest } from "../../types/types";

export const submitAttempt = async (
  examId: number,
  userId: number,
  answers: AnswerRequest[],
) => {
  const res = await api.post(`/api/attempt/${examId}/${userId}`, answers);

  return res.data;
};

export const getAttemptReview = async (attemptId: string) => {
  const res = await api.get(`/api/attempt/${attemptId}/review`);
  return res.data;
};

export const startExamAttempt = async (examId: number, userId: number) => {
  const res = await api.post(`/api/attempt/start/${examId}/${userId}`);
  return res.data;
};