# MedReserve AI - Backend

Spring Boot backend for the MedReserve AI doctor appointment system.

## Features

### Phase 1 - Authentication & Authorization ✅
- JWT-based authentication with access and refresh tokens
- Role-based access control (Patient, Doctor, Admin, Master Admin)
- User registration and login
- Password encryption with BCrypt
- Automatic role and master admin initialization

## Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: MySQL 8.0 (H2 for testing)
- **Security**: Spring Security + JWT
- **Documentation**: OpenAPI/Swagger
- **Testing**: JUnit 5, Spring Boot Test
- **Build Tool**: Maven

## Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8.0+ (for production)

## Setup

### 1. Clone and Navigate
```bash
cd backend
```

### 2. Environment Configuration
Copy the example environment file:
```bash
cp .env.example .env
```

Edit `.env` with your configuration:
```env
# Database
DB_URL=jdbc:mysql://localhost:3306/medreserve_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Email (for notifications)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### 3. Database Setup
Create MySQL database:
```sql
CREATE DATABASE medreserve_db;
```

### 4. Build and Run
```bash
# Build
mvn clean compile

# Run tests
mvn test

# Start application
mvn spring-boot:run
```

## API Documentation

Once running, access Swagger UI at:
- http://localhost:8080/api/swagger-ui.html

API docs available at:
- http://localhost:8080/api/api-docs

## Default Credentials

Master Admin account is automatically created:
- **Email**: admin@medreserve.com
- **Password**: MasterAdmin@123

## Testing

### Unit Tests
```bash
mvn test -Dtest=JwtUtilsTest
```

### Integration Tests
```bash
mvn test -Dtest=MedReserveApplicationTest
```

### All Tests
```bash
mvn test
```

## Docker

### Build Image
```bash
docker build -t medreserve-backend .
```

### Run Container
```bash
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/medreserve_db \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=password \
  medreserve-backend
```

## API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/signout` - User logout

### Health Check
- `GET /api/actuator/health` - Application health status

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/medreserve/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── security/        # Security components
│   │   │   └── service/         # Business logic
│   │   └── resources/
│   │       └── application.yml  # Application configuration
│   └── test/                    # Test files
├── Dockerfile                   # Docker configuration
├── pom.xml                     # Maven dependencies
└── README.md                   # This file
```

## Development

### Adding New Features
1. Create entity classes in `entity/`
2. Add repositories in `repository/`
3. Implement services in `service/`
4. Create controllers in `controller/`
5. Add DTOs in `dto/`
6. Write tests

### Code Style
- Use Lombok for reducing boilerplate
- Follow Spring Boot conventions
- Add proper validation annotations
- Include comprehensive tests

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check MySQL is running
   - Verify credentials in `.env`
   - Ensure database exists

2. **JWT Token Issues**
   - Check JWT_SECRET is set
   - Verify token expiration settings

3. **Port Already in Use**
   - Change SERVER_PORT in `.env`
   - Kill process using port 8080

### Logs
Application logs are written to:
- Console (development)
- `logs/medreserve.log` (production)

## Next Phases

- [ ] Phase 2: Appointment Management System
- [ ] Phase 3: Prescription & Report Uploads
- [ ] Phase 4: Real-Time Chat (WebSocket)
- [ ] Phase 5: ML Microservice (FastAPI)
- [ ] Phase 6: Chatbot API
- [ ] Phase 7: Smart Features
- [ ] Phase 8: Admin Panel Backend
#
