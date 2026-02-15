import api from "../../api/axiosConfig";

export interface SecurityLogRequest {
  userId: number;
  examId: number;
  activityType:
  | "TAB_SWITCH"
  | "FULLSCREEN_EXIT"
  | "COPY_PASTE"
  | "MULTIPLE_LOGIN"
  | "LATE_SUBMISSION";
  details: string;
}

export const logSecurityEvent = async (
  attemptId: number,
  eventType: string,
) => {
  try {
    await api.post("/api/security/log", {
      attemptId,
      activityType: eventType,
      details: "User performed " + eventType,
    });
  } catch (err) {
    console.error("Security log failed:", err);
  }
};
