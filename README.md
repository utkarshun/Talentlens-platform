# TalentLens: AI-Powered Secure Assessment & Interview Prep

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

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

To run the application locally, please follow the detailed instructions in [DEPLOYMENT.md](DEPLOYMENT.md).

1.  Clone the repository:
    ```bash
    git clone https://github.com/utkarshun/Talentlens-platform.git
    ```
2.  Navigate to the project root:
    ```bash
    cd Talentlens-platform
    ```
3.  Start the application:
    ```bash
    docker-compose up --build
    ```

## Contributing

Contributions are always welcome! Please read the [Contributing Guide](CONTRIBUTING.md) and [Code of Conduct](CODE_OF_CONDUCT.md) first.

## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.
