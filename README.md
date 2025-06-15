# 🔐 Demo API with JWT Authentication

This is a Spring Boot REST API that simulates a real-world user login and profile retrieval scenario using **JWT-based authentication**.

## 🔧 Tech Stack
- Java 17+
- Spring Boot
- JWT (JJWT 0.12.6)
- REST API
- Maven

## 📌 Features
- `/api/users/login` — Authenticates user, returns JWT
- `/api/users/profile` — Secured endpoint, accessible only with valid JWT
- 🔐 Token verification using `Authorization: Bearer <token>`
- Simple hardcoded user simulation (`admin` / `pass`)
- Proper HTTP status handling

## 📦 How to Run

```bash
git clone https://github.com/<your-username>/demo-api.git
cd demo-api
mvn spring-boot:run
