# AcmePlex Movie Theater Ticket Reservation System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A full-stack movie theater ticket reservation system built with Spring Boot. This application allows users to browse movies, select showtimes, book seats, and manage ticket cancellations with an automated refund system.

**ENSF 614 - Fall 2024 Term Project**
University of Calgary - Principles of Software Design

## Features

- **Movie Browsing**: View movies currently showing and coming soon
- **Theatre Selection**: Browse movies by theatre location
- **Showtime Management**: Select from morning, afternoon, and evening sessions
- **Seat Selection**: Interactive seat map with real-time availability
- **Secure Payments**: Process ticket purchases with credit card validation
- **Ticket Cancellation**: 72-hour cancellation policy with automatic refunds
- **Credit System**: Store credits for guest user cancellations
- **User Authentication**: Secure login with BCrypt password hashing
- **REST API**: Full RESTful API with OpenAPI/Swagger documentation
- **Admin Dashboard**: Manage notifications for upcoming movies

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Layer                              │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐│
│  │   Thymeleaf UI   │  │   REST API       │  │   Swagger UI     ││
│  │   (Browser)      │  │   (/api/*)       │  │   (/swagger-ui)  ││
│  └────────┬─────────┘  └────────┬─────────┘  └────────┬─────────┘│
└───────────┼─────────────────────┼─────────────────────┼──────────┘
            │                     │                     │
┌───────────▼─────────────────────▼─────────────────────▼──────────┐
│                      Controller Layer                             │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │  AuthController │ MovieController │ TicketController │ etc.  ││
│  └──────────────────────────────────────────────────────────────┘│
└───────────────────────────────┬──────────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────────┐
│                       Service Layer                               │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │   TicketPaymentService   │   CancellationService             ││
│  └──────────────────────────────────────────────────────────────┘│
└───────────────────────────────┬──────────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────────┐
│                      Repository Layer                             │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │  UserRepo │ MovieRepo │ TicketRepo │ SeatRepo │ CreditRepo   ││
│  └──────────────────────────────────────────────────────────────┘│
└───────────────────────────────┬──────────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────────┐
│                        Database Layer                             │
│                    ┌──────────────────┐                          │
│                    │   MySQL / H2     │                          │
│                    └──────────────────┘                          │
└──────────────────────────────────────────────────────────────────┘
```

## Quick Start

### Using Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/yourusername/AcmePlex-Movie-Theater-Ticket-Reservation-App.git
cd AcmePlex-Movie-Theater-Ticket-Reservation-App

# Start the application with Docker Compose
docker-compose up -d

# Access the application
# Web UI: http://localhost:8080
# API Docs: http://localhost:8080/swagger-ui.html
# Database Admin: http://localhost:8081 (Adminer)
```

### Local Development

#### Prerequisites

- Java 17 or higher
- Gradle 8.x (or use the included wrapper)

#### Run with H2 (In-Memory Database)

```bash
# Clone and navigate to the project
git clone https://github.com/yourusername/AcmePlex-Movie-Theater-Ticket-Reservation-App.git
cd AcmePlex-Movie-Theater-Ticket-Reservation-App

# Run the application
./gradlew bootRun

# Access the application at http://localhost:8080
```

#### Default Test Accounts

| Username | Password | Role |
|----------|----------|------|
| guest | password123 | Guest User |
| admin | admin123 | Admin |
| testuser | test123 | Regular User |

## API Documentation

The API is documented using OpenAPI 3.0 and can be accessed at:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

### API Endpoints Overview

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/movies` | GET | List all movies |
| `/api/movies/{id}` | GET | Get movie by ID |
| `/api/theatres` | GET | List all theatres |
| `/api/showtimes` | GET | List all showtimes |
| `/api/showtimes/{id}/seats` | GET | Get seats for a showtime |
| `/api/tickets/purchase` | POST | Purchase tickets |
| `/api/tickets/{ref}/cancel` | POST | Cancel a ticket |
| `/api/auth/login` | POST | User login |
| `/api/auth/register` | POST | User registration |

## Project Structure

```
src/
├── main/
│   ├── java/com/example/movieticket/
│   │   ├── config/          # Security & API configuration
│   │   ├── controller/      # MVC & REST controllers
│   │   │   └── api/         # REST API controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── request/     # Request DTOs
│   │   │   └── response/    # Response DTOs
│   │   ├── entity/          # JPA Entities
│   │   ├── exception/       # Custom exceptions
│   │   ├── repository/      # Spring Data repositories
│   │   └── service/         # Business logic services
│   └── resources/
│       ├── static/          # CSS, JavaScript
│       ├── templates/       # Thymeleaf templates
│       └── application.properties
└── test/
    └── java/com/example/movieticket/
        ├── controller/      # Controller tests
        ├── repository/      # Repository tests
        └── service/         # Service tests
```

## Tech Stack

| Category | Technology |
|----------|------------|
| Backend Framework | Spring Boot 3.3.5 |
| Language | Java 17 |
| Database | MySQL 8.0 / H2 (dev) |
| Security | Spring Security + BCrypt |
| API Documentation | SpringDoc OpenAPI |
| Template Engine | Thymeleaf |
| Build Tool | Gradle |
| Containerization | Docker |
| CI/CD | GitHub Actions |

## Testing

```bash
# Run all tests
./gradlew test

# View test report
open build/reports/tests/test/index.html
```

## Configuration

### Environment Variables (Docker)

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:mysql://db:3306/acmeplex` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `acmeplex` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `acmeplex123` |
| `SPRING_PROFILES_ACTIVE` | Active profile | `docker` |

## Business Rules

### User Types

- **Ordinary Users**: Basic ticket booking with a 15% cancellation fee
- **Registered Users**: $20 annual membership with no cancellation fees and early access

### Ticket Cancellation Policy

- Cancellations allowed up to **72 hours** before showtime
- **Registered Users**: Full refund to original payment method
- **Guest Users**: 85% refund + 15% store credit (valid for 1 year)

### Coming Soon Movies

- Only visible to registered users
- Early booking available for members (10% of seats reserved)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

Built as part of ENSF 614 (Fall 2024) course project at the University of Calgary.
