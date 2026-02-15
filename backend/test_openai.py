import requests
import json
import time

BASE_URL = "http://localhost:8080/api"

def test():
    # Wait for server to be ready (naive check)
    print("Waiting for server...")
    time.sleep(5) 

    # 1. Login
    print("Logging in...")
    login_payload = {"email": "admin@secureexam.com", "password": "admin123"}
    try:
        resp = requests.post(f"{BASE_URL}/auth/login", json=login_payload)
        resp.raise_for_status()
        data = resp.json()
        token = data.get("accessToken") or data.get("token") # Adjust based on actual response structure
        if not token:
             print("No token found in response:", data)
             return
        print("Login successful.")
    except Exception as e:
        print(f"Login failed: {e}")
        try:
            print("Response text:", resp.text)
        except:
            pass
        return

    headers = {"Authorization": f"Bearer {token}"}

    # 2. Get Exams
    print("Fetching exams...")
    try:
        resp = requests.get(f"{BASE_URL}/exams", headers=headers)
        resp.raise_for_status()
        exams = resp.json()
        if not exams:
            print("No exams found.")
            return
        
        target_exam = exams[0]
        exam_id = target_exam["id"]
        print(f"Found exam: {target_exam['title']} (ID: {exam_id})")
    except Exception as e:
        print(f"Fetch exams failed: {e}")
        try:
             print("Response text:", resp.text)
        except:
             pass
        return

    # 3. Generate Questions
    print(f"Generating questions for Exam ID {exam_id}...")
    try:
        resp = requests.post(f"{BASE_URL}/exams/{exam_id}/generate-questions?count=3", headers=headers)
        if resp.status_code == 200:
             print("Success! Generated Questions:")
             print(json.dumps(resp.json(), indent=2))
        else:
             print(f"Failed to generate questions. Status: {resp.status_code}")
             print(resp.text)
    except Exception as e:
        print(f"Generate questions failed: {e}")

if __name__ == "__main__":
    test()
