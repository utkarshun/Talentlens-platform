import requests
import json
import time

BASE_URL = "http://localhost:8080/api"

def test_review():
    print("Logging in as Admin...")
    login_payload = {"email": "admin@secureexam.com", "password": "admin123"}
    try:
        resp = requests.post(f"{BASE_URL}/auth/login", json=login_payload)
        resp.raise_for_status()
        token = resp.json().get("accessToken")
        headers = {"Authorization": f"Bearer {token}"}
        print("Login successful.")

        # 1. Get All Attempts
        print("Fetching all attempts...")
        resp = requests.get(f"{BASE_URL}/attempt/all", headers=headers)
        resp.raise_for_status()
        attempts = resp.json()
        
        if not attempts:
            print("No attempts found to review.")
            return

        target_attempt = attempts[0]
        attempt_id = target_attempt["id"]
        print(f"Testing review for Attempt ID: {attempt_id}")

        # 2. Get Review Details
        print(f"Fetching review details for attempt {attempt_id}...")
        resp = requests.get(f"{BASE_URL}/attempt/{attempt_id}/review", headers=headers)
        
        if resp.status_code == 200:
            print("Success! Review Details:")
            print(json.dumps(resp.json(), indent=2))
        else:
            print(f"Failed to fetch review. Status: {resp.status_code}")
            print(resp.text)

    except Exception as e:
        print(f"Test failed: {e}")

if __name__ == "__main__":
    test_review()
