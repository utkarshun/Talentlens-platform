# TalentLens: AI-Powered Secure Assessment & Interview Prep

A comprehensive full-stack solution designed to streamline the recruitment lifecycle through secure, AI-proctored examinations and targeted technical interview preparation.

## Key Features

-   **Secure Environment**: Fullscreen enforcement, tab-switch detection, and anti-copy/paste mechanisms.
-   **AI Proctoring**: Automated suspicious activity tracking and scoring.
-   **Role-Based Access**: Distinct portals for Students and Administrators.
-   **Real-time Analytics**: Live monitoring of exam attempts and results.
-   **Scalable Architecture**: Built with Spring Boot (Java) and React (TypeScript), containerized with Docker.

## Tech Stack

-   **Backend**: Java 21, Spring Boot 3, Spring Security (JWT), PostgreSQL/H2, Maven.
-   **Frontend**: React 18, TypeScript, Material UI, Vite.
-   **DevOps**: Docker, Docker Compose, Nginx.

## Getting Started (Docker)

1.  Clone the repository:
    ```bash
    git clone https://github.com/utkarshun/secure-exam-platform.git
    ```
2.  Navigate to the project root:
    ```bash
    cd secure-exam-platform
    ```
3.  Start the application:
    ```bash
    docker-compose up --build
    ```
4.  Access the application:
    -   Frontend: `http://localhost:80`
    -   Backend API: `http://localhost:8080`

## License

MIT License
