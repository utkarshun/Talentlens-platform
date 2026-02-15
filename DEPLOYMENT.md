# Secure Exam Platform Deployment Guide

This guide explains how to deploy the Secure Exam Platform using Docker Compose.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed on your machine or server.
- Git (to clone the repository).

## Deployment Steps

1.  **Clone the repository** (if you haven't already):
    ```bash
    git clone https://github.com/utkarshun/Talentlens-platform.git
    cd Talentlens-platform
    ```

2.  **Configuration**:
    - The backend production configuration is in `backend/src/main/resources/application-prod.properties`.
    - The `docker-compose.yml` file defines the services (frontend, backend, database).
    - **Important**: Create a `.env` file in the root directory to store your secrets (optional but recommended for production):
      ```ini
      POSTGRES_USER=postgres
      POSTGRES_PASSWORD=secure_password
      POSTGRES_DB=secure_exam
      OPENAI_API_KEY=your_openai_api_key_here
      ```
      *Note: The current `docker-compose.yml` has default values for development convenience, but you should override them in a production environment.*

3.  **Build and Run**:
    Run the following command to build the Docker images and start the services:
    ```bash
    docker-compose up --build -d
    ```
    - `--build`: Rebuilds the images if you made changes.
    - `-d`: Runs the containers in detached mode (in the background).

4.  **Access the Application**:
    - **Frontend**: http://localhost (Port 80)
    - **Backend API**: http://localhost:8080/api

## Troubleshooting

- **Database Connection Issues**:
  - Ensure the `db` service is healthy. Check logs with `docker-compose logs db`.
  - If the backend fails to connect, it might be starting before the database is ready. The `depends_on` in `docker-compose.yml` helps, but sometimes a retry mechanism is needed. Docker Compose usually handles this well enough for this setup.

- **Port Conflicts**:
  - If port 80 or 8080 is already in use, modify the `ports` mapping in `docker-compose.yml`:
    ```yaml
    ports:
      - "8081:80" # Maps host port 8081 to container port 80
    ```

- **Rebuilding**:
  - If you change code, you must rebuild:
    ```bash
    docker-compose down
    docker-compose up --build -d
    ```

## Project Structure for Deployment

- `docker-compose.yml`: Orchestrates the services.
- `frontend/Dockerfile`: Builds the React app and serves it with Nginx.
- `frontend/nginx.conf`: Nginx configuration for the frontend container.
- `backend/Dockerfile`: Builds the Spring Boot JAR and runs it.
