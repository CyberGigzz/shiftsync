# ShiftSync API

A RESTful Spring Boot application for managing employee shifts and schedules. This capstone project showcases a modern, robust backend architecture featuring JWT-based security, role-based access control, and a dynamic, process-driven workflow for shift swapping.

## Table of Contents

- [ShiftSync API](#shiftsync-api)
  - [Table of Contents](#table-of-contents)
  - [Key Features](#key-features)
  - [Tech Stack](#tech-stack)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation \& Setup](#installation--setup)
    - [Configuration](#configuration)
    - [Running the Application](#running-the-application)
  - [API Documentation](#api-documentation)
    - [Swagger UI](#swagger-ui)
    - [Database Access (pgAdmin)](#database-access-pgadmin)
  - [Roles and Permissions](#roles-and-permissions)
  - [Authentication](#authentication)
  - [API Endpoints](#api-endpoints)
  - [Testing](#testing)

## Key Features

- ✅ **Full CRUD Operations:** For Shifts and Job Positions.
- ✅ **JWT-Based Security:** Secure, stateless authentication using JSON Web Tokens.
- ✅ **Role-Based Access Control (RBAC):** Two distinct user roles (`MANAGER`, `EMPLOYEE`) with clearly defined permissions.
- ✅ **Dynamic Shift Swapping:** A complete, multi-step workflow for employees to request, accept, and get manager approval for shift trades.
- ✅ **Employee Availability Management:** Users can set their weekly availability to assist managers in scheduling.
- ✅ **Database Migrations:** Schema and seed data are managed with **Flyway**, ensuring a consistent and version-controlled database.
- ✅ **Automatic Data Seeding:** The database is populated with a realistic, future-proof set of sample data on startup for easy testing and demonstration.
- ✅ **API Documentation:** Automatically generated and interactive API documentation via **Springdoc OpenAPI (Swagger)**.
- ✅ **Unit Tested:** Core business logic in the service layer is covered by unit tests using **JUnit 5** and **Mockito**.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
  - **Spring Web:** For building RESTful APIs.
  - **Spring Data JPA:** For database interaction with Hibernate.
  - **Spring Security:** For authentication and authorization.
- **Database:**
  - **PostgreSQL:** Containerized with Docker for a consistent development environment.
  - **Flyway:** For database schema migrations and data seeding.
- **Tooling:**
  - **Maven:** For project and dependency management.
  - **Docker & Docker Compose:** For orchestrating the database and admin tools.
  - **Lombok:** To reduce boilerplate code.
- **API & Documentation:**
  - **Springdoc OpenAPI (Swagger):** For generating interactive API documentation.
- **Testing:**
  - **JUnit 5 & Mockito:** For unit testing the service layer.

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

- **JDK 17** or newer.
- **Docker** and **Docker Compose**.
- An IDE of your choice (e.g., IntelliJ IDEA, VS Code).

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd <your-project-directory>
    ```

2.  **Start the infrastructure:**
    This command will start the PostgreSQL database and pgAdmin containers in the background.
    ```bash
    docker-compose up -d
    ```

### Configuration

The main configuration is located in `src/main/resources/application.properties`.

- **JWT Secret:** The `application.security.jwt.secret-key` is used for signing tokens. The default value is for development only. For a production environment, this should be replaced with a strong, securely stored secret.
- **Database:** The application is pre-configured to connect to the PostgreSQL instance defined in the `docker-compose.yml` file.

### Running the Application

1.  **From your IDE:**
    - Open the project as a Maven project.
    - Locate the `ShiftsyncApplication.java` class in `src/main/java/com/epam/shiftsync`.
    - Run the `main` method.

The application will start on `http://localhost:8080`.

## API Documentation

### Swagger UI

Once the application is running, you can access the interactive Swagger UI documentation at:
- **URL:** `http://localhost:8080/swagger-ui.html`

Here you can view all available endpoints, see their request/response structures, and test them directly from your browser. Use the "Authorize" button to apply your JWT for protected endpoints.

### Database Access (pgAdmin)

A pgAdmin container is included for easy database management.
- **URL:** `http://localhost:5050`
- **Login Email:** `admin@example.com`
- **Login Password:** `admin`

To connect to the database from within pgAdmin, create a new server connection with the following details:
- **Host name/address:** `shiftsync_db` (the Docker service name)
- **Port:** `5432`
- **Maintenance database:** `shiftsync_db`
- **Username:** `shiftsync_user`
- **Password:** `shiftsync_password`

## Roles and Permissions

The application uses a two-role system for access control.

| Role | Permissions |
| :--- | :--- |
| **MANAGER** | - Can do everything an EMPLOYEE can.<br>- Full CRUD access to Shifts and Positions.<br>- Can view the full list of all users.<br>- Can give final approval/denial for shift swap requests. |
| **EMPLOYEE** | - Can view their own profile and the schedule.<br>- Can set their weekly availability.<br>- Can initiate and respond to shift swap requests. |

## Authentication

Authentication is handled via JWT.

1.  Send a `POST` request to `/api/auth/login` with your email and password.
2.  The API will return a JWT token upon successful authentication.
3.  For all subsequent requests to protected endpoints, include the token in the `Authorization` header.
    ```
    Authorization: Bearer <your_jwt_token>
    ```

The `V2__Seed_Initial_Data.sql` migration script creates several sample users with the password `password123`. For example:
- `manager@example.com` (MANAGER)
- `jane.doe@example.com` (EMPLOYEE)

## API Endpoints

A summary of the main API endpoints:

| Method | Endpoint | Description | Required Role(s) |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user (as EMPLOYEE). | Public |
| `POST` | `/api/auth/login` | Log in to get a JWT. | Public |
| `POST` | `/api/positions` | Create a new job position. | `MANAGER` |
| `GET` | `/api/positions` | Get all job positions. | Authenticated |
| `POST` | `/api/shifts` | Create a new shift. | `MANAGER` |
| `GET` | `/api/shifts` | Get shifts for a date range. | Authenticated |
| `PUT` | `/api/shifts/{id}` | Update a shift. | `MANAGER` |
| `DELETE` | `/api/shifts/{id}` | Delete a shift. | `MANAGER` |
| `GET` | `/api/users/me` | Get the current user's profile. | Authenticated |
| `GET` | `/api/users` | Get a list of all users. | `MANAGER` |
| `PUT` | `/api/users/me/availability` | Update the current user's availability. | Authenticated |
| `POST` | `/api/swaps` | Initiate a new shift swap request. | Authenticated |
| `GET` | `/api/swaps/me` | Get swaps involving the current user. | Authenticated |
| `POST` | `/api/swaps/{id}/respond` | Respond to a swap request. | `EMPLOYEE` |
| `POST` | `/api/swaps/{id}/approve` | Approve/deny a swap request. | `MANAGER` |

## Testing

This project includes a suite of unit tests for the service layer, ensuring the core business logic is reliable and correct. Tests are built using **JUnit 5** and **Mockito**.

To run all tests from the command line, use the Maven wrapper:
```bash
./mvnw test
```
To run tests from your IDE, navigate to the `src/test/java` directory, right-click on a test class or package, and select "Run Tests".