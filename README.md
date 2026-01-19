# PequeñosPasos – Nursery Management System

<p align="center">
  <img src="https://skillicons.dev/icons?i=java,spring,angular,ts,mysql,kotlin,androidstudio,githubactions&perline=8" />
</p>

PequeñosPasos is a nursery management system designed to handle sensitive child-related data in a structured, secure, and role-based manner.  
The project focuses on clean backend architecture, access control, and CI-driven quality controls, while keeping a realistic scope.

This repository represents an **academic project enhanced with professional engineering practices**, developed and maintained as a personal portfolio project.

---

## Project Status

- Academic project **completed and graded (9/10)**
- Actively improved as a **personal engineering and portfolio-oriented project**
- **Not deployed to production**
- Intended for **local execution and code review**

---

## System Overview

PequeñosPasos is composed of three main components:

### Backend (Core System)
- REST API responsible for business logic, security, and data persistence
- Role-based access control for sensitive data

### Web Frontend (Educators & Administration)
- Angular-based web application
- Used by educators and administrators to manage nursery operations

### Mobile App (Parents)
- Native Android application (Kotlin)
- Read-only access for parents to view information related to their children
- Authentication linked to backend user roles

---

## Technical Stack

<p align="center">
  <img src="https://skillicons.dev/icons?i=java,spring,mysql,hibernate,maven&perline=10" />
</p>

<p align="center">
  <img src="https://skillicons.dev/icons?i=angular,ts,primeng,html,css&perline=10" />
</p>

<p align="center">
  <img src="https://skillicons.dev/icons?i=kotlin,androidstudio&perline=10" />
</p>

<p align="center">
  <img src="https://skillicons.dev/icons?i=githubactions,git&perline=10" />
</p>

### Backend
- Java 17
- Spring Boot
- REST API architecture
- Spring Security (role-based access control)
- JPA / Hibernate
- MySQL (development)
- H2 (test profile)
- Maven

### Frontend (Web)
- Angular 19
- PrimeNG
- TypeScript

### Mobile App
- Kotlin
- Android (native)
- Parent-facing, read-only application

---

## Core Features

- Child management
- Parent and educator management
- Attendance tracking
- Daily activity logging
- Meals and hygiene records
- Role-based access control
- Clear separation between educator/admin and parent permissions

---

## Security & Access Control

Given the nature of the data (children and families), the system was designed with security in mind:

- Role-based authorization
- Restricted access to child data
- Clear separation between:
  - Administrators
  - Educators
  - Parents
- Backend-enforced access rules (not delegated to the frontend)

While not deployed publicly, the system is structured to align with **GDPR-aware architectural principles**.

---

## Quality Assurance & CI

This project includes a **working CI pipeline** focused on reliability and portability.

### Backend QA
- JUnit 5
- Mockito
- Unit tests for service-layer business rules
- Context loading tests using an isolated H2 test profile
- Test execution in CI

### Frontend QA
- Dependency installation via `npm ci`
- Angular production build verification executed in CI

### CI Pipeline
- GitHub Actions
- Separate jobs for backend and frontend
- CI validates:
  - Backend tests
  - Frontend build
- Artifacts generated:
  - JUnit test reports
  - Frontend build output

This setup ensures that:
- The backend logic remains stable
- The frontend remains buildable in a clean environment
- The project is portable and reproducible

---

## Running the Project Locally

### Backend
```bash
cd backend
./mvnw test
./mvnw spring-boot:run
```

### Frontend (Web)
```bash
cd web-app/guarderia-frontend
npm ci
npm run build
npm start
```

### Notes on Security Dependencies

Some frontend dependencies currently report known vulnerabilities via npm audit.
Fixing them requires a framework patch upgrade blocked by Node.js version constraints.
- No public deployment
- No exposed production environment
- Risk documented and intentionally deferred

This decision reflects realistic scope management rather than ignoring the issue.

### Purpose of This Repository

This project is intended to demonstrate:
- Backend architecture and service-layer testing
- CI-driven quality control
- Role-based access design
- Practical engineering decision-making
- Ability to evolve an academic project into a professional-grade codebase.

### Author

Developed and maintained by **Víctor López** as part of an academic project, extended with professional engineering practices.