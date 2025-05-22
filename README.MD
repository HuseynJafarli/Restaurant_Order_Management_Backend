# RombApp Backend

This is a **Gradle-based** Spring Boot backend for the **RombApp**, a restaurant management system. The project includes features for managing orders, menu items, restaurant tables, and staff users, with role-based JWT authentication.

## 💪 Tech Stack

* **Spring Boot** 3.x
* **Gradle**
* **Java 17**
* **PostgreSQL**
* **RabbitMQ** (for async messaging)
* **Spring Security** with JWT
* **JUnit & MockMvc** for testing

---

## 📁 Project Structure

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── romb
│   │           └── rombApp
│   │               ├── config
│   │               ├── controller
│   │               ├── dto
│   │               ├── exception
│   │               ├── messaging
│   │               ├── model
│   │               ├── repository
│   │               ├── service
│   │               └── RombAppApplication.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com
            └── romb
                └── rombApp
```

---

## ▶️ How to Run

### Prerequisites

* Java 17
* Gradle
* PostgreSQL running on `localhost:5432`
* RabbitMQ running on `localhost:5672`

### 1. Configure Database

Update the `application.yml` with your local PostgreSQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/rombdb
    username: your_username
    password: your_password
```

### 2. Run RabbitMQ

You can use Docker:

```bash
docker run -d --hostname rmq --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

### 3. Start the App

```bash
gradle bootRun
```

### 4. Access H2 Console (for test)

Available at `http://localhost:8080/h2-console`

Make sure H2 access is permitted in security config:

```java
.authorizeHttpRequests(auth -> auth.requestMatchers("/h2-console/**").permitAll())
.csrf(AbstractHttpConfigurer::disable)
.headers(headers -> headers.frameOptions().disable())
```

---

## 🔐 Authentication

* Uses JWT Bearer tokens
* Role-based access: `MANAGER`, `WAITER`, `KITCHEN`
* Endpoints:

  * `/api/auth/register`
  * `/api/auth/login`

Example login response:

```json
{
  "token": "eyJhbGci..."
}
```

Use this token in requests:

```
Authorization: Bearer <token>
```

---

## ✅ Running Tests

```bash
gradle test
```

---

## 🧑‍💻 Contributing

Pull requests welcome. For major changes, open an issue first.

---

## 📝 License

[MIT](LICENSE)
