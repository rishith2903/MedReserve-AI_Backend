# 🏥 MedReserve AI - Backend Service

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive Spring Boot backend service for the MedReserve AI healthcare management platform, providing secure APIs for patient management, appointment booking, medical records, and AI-powered health services.

## 🌟 Features

### 🔐 Authentication & Authorization
- **JWT-based Authentication** with access and refresh tokens
- **Role-based Access Control** (Patient, Doctor, Admin, Master Admin)
- **Secure Password Hashing** with BCrypt
- **Session Management** with token refresh capabilities
- **Multi-factor Authentication** support

### 👥 User Management
- **Patient Registration** and profile management
- **Doctor Profiles** with specializations and availability
- **Admin Dashboard** for user management
- **Role-based Permissions** and access control
- **User Analytics** and reporting

### 📅 Appointment System
- **Online Booking** with real-time availability
- **Schedule Management** for doctors
- **Appointment Notifications** and reminders
- **Calendar Integration** with time slot management
- **Booking Conflicts** prevention and resolution

### 🏥 Medical Records
- **Electronic Health Records (EHR)** management
- **Medical Report Upload** and secure storage
- **Prescription Management** and tracking
- **Patient History** and medical documentation
- **HIPAA-compliant** data handling

### 🤖 AI Integration
- **ML Service Integration** for symptom analysis
- **Chatbot Service** for patient support
- **Predictive Analytics** for health insights
- **Smart Recommendations** based on patient data

### 📊 Analytics & Monitoring
- **Real-time Metrics** with Spring Boot Actuator
- **Performance Monitoring** and health checks
- **Audit Logging** for compliance
- **Rate Limiting** and security monitoring

## 🏗️ Tech Stack

- **Framework**: Spring Boot 3.2.0 with Java 17
- **Database**: PostgreSQL (Production) / H2 (Development)
- **Security**: Spring Security 6.x with JWT
- **Documentation**: OpenAPI 3.0 (Swagger UI)
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Build Tool**: Maven 3.9+
- **Deployment**: Docker, Render Platform
- **Monitoring**: Spring Boot Actuator, Micrometer

## 📋 Prerequisites

- **Java 17+** (OpenJDK recommended)
- **Maven 3.9+** for dependency management
- **PostgreSQL 13+** (for production)
- **Docker** (optional, for containerized deployment)
- **Git** for version control

## 🚀 Quick Start

### 1. Clone and Navigate
```bash
git clone <repository-url>
cd MedReserve/backend
```

### 2. Environment Configuration

Create environment configuration file:
```bash
# Copy example environment file (if available)
cp .env.example .env
```

Configure environment variables in `.env` or set them directly:
```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/medreserve_db
DB_USERNAME=postgres
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_here_minimum_256_bits
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Server Configuration
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://yourdomain.com

# Email Configuration (for notifications)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=noreply@medreserve.com

# AI Services Configuration
ML_SERVICE_URL=http://localhost:8001
CHATBOT_SERVICE_URL=http://localhost:8002

# File Upload Configuration
FILE_UPLOAD_DIR=./uploads
MAX_FILE_SIZE=10MB
MAX_REQUEST_SIZE=10MB

# Logging Configuration
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_MEDRESERVE=DEBUG
```

### 3. Database Setup

#### Option A: PostgreSQL (Recommended for Production)
```bash
# Install PostgreSQL
# Ubuntu/Debian
sudo apt-get install postgresql postgresql-contrib

# macOS
brew install postgresql

# Create database
sudo -u postgres createdb medreserve_db
sudo -u postgres createuser --interactive
```

#### Option B: H2 Database (Development Only)
No setup required - H2 runs in-memory for development.

### 4. Build and Run

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Start application (development mode)
mvn spring-boot:run

# Or start with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Build JAR for production
mvn clean package -DskipTests
java -jar target/medreserve-backend-1.0.0.jar
```

### 5. Verify Installation

Check if the application is running:
```bash
# Health check
curl http://localhost:8080/actuator/health

# API documentation
open http://localhost:8080/swagger-ui.html
```

## 📚 API Documentation

### Swagger UI
Interactive API documentation is available at:
- **Development**: http://localhost:8080/swagger-ui.html
- **Production**: https://your-domain.com/swagger-ui.html

### OpenAPI Specification
Raw API documentation:
- **JSON Format**: http://localhost:8080/v3/api-docs
- **YAML Format**: http://localhost:8080/v3/api-docs.yaml

### Postman Collection
Import the Postman collection for easy API testing:
```bash
# Export collection (if available)
curl -o medreserve-api.postman_collection.json http://localhost:8080/v3/api-docs
```

## 🔐 Demo Credentials

The application automatically creates demo accounts for testing:

### Patient Account
- **Email**: `patient@medreserve.com`
- **Password**: `password123`
- **Role**: PATIENT
- **Access**: Patient dashboard, appointments, medical records

### Doctor Account
- **Email**: `doctor@medreserve.com`
- **Password**: `password123`
- **Role**: DOCTOR
- **Access**: Doctor dashboard, patient management, appointments

### Admin Account
- **Email**: `demo@medreserve.com`
- **Password**: `password123`
- **Role**: ADMIN
- **Access**: Admin dashboard, user management, system settings

### Master Admin Account
- **Email**: `admin@medreserve.com`
- **Password**: `MasterAdmin@123`
- **Role**: MASTER_ADMIN
- **Access**: Full system access, user management, system configuration

## 🔗 API Endpoints

### Authentication Endpoints
```http
POST   /auth/signup          # User registration
POST   /auth/login           # User login
POST   /auth/refresh         # Refresh access token
POST   /auth/logout          # User logout
GET    /auth/me              # Get current user info
```

### User Management
```http
GET    /users                # Get all users (admin only)
GET    /users/{id}           # Get user by ID
PUT    /users/{id}           # Update user profile
DELETE /users/{id}           # Delete user (admin only)
GET    /users/profile        # Get current user profile
PUT    /users/profile        # Update current user profile
```

### Doctor Management
```http
GET    /doctors              # Get all doctors
GET    /doctors/{id}         # Get doctor details
GET    /doctors/specialties  # Get all specialties
GET    /doctors/search       # Search doctors by specialty/location
PUT    /doctors/{id}/availability  # Update doctor availability
```

### Appointment Management
```http
GET    /appointments         # Get user appointments
POST   /appointments         # Book new appointment
GET    /appointments/{id}    # Get appointment details
PUT    /appointments/{id}    # Update appointment
DELETE /appointments/{id}    # Cancel appointment
GET    /appointments/doctor/{doctorId}  # Get doctor's appointments
```

### Medical Records
```http
GET    /medical-records      # Get patient's medical records
POST   /medical-records      # Create new medical record
GET    /medical-records/{id} # Get specific medical record
PUT    /medical-records/{id} # Update medical record
DELETE /medical-records/{id} # Delete medical record
POST   /medical-records/upload  # Upload medical documents
```

### AI Services Integration
```http
POST   /ai/symptom-checker   # Analyze symptoms
POST   /ai/chatbot           # Chat with AI assistant
GET    /ai/health-tips       # Get personalized health tips
POST   /ai/risk-assessment   # Health risk assessment
```

### System Endpoints
```http
GET    /actuator/health      # Application health check
GET    /actuator/info        # Application information
GET    /actuator/metrics     # Application metrics
GET    /actuator/prometheus  # Prometheus metrics
```

## 🧪 Testing

### Unit Tests
```bash
# Run specific test class
mvn test -Dtest=JwtUtilsTest

# Run specific test method
mvn test -Dtest=AuthControllerTest#testLogin

# Run tests with coverage
mvn test jacoco:report
```

### Integration Tests
```bash
# Run integration tests
mvn test -Dtest=*IntegrationTest

# Run specific integration test
mvn test -Dtest=MedReserveApplicationTest
```

### API Testing
```bash
# Run all tests
mvn test

# Run tests with specific profile
mvn test -Dspring.profiles.active=test

# Run tests and generate reports
mvn clean test site
```

### Manual API Testing
```bash
# Test authentication
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"patient@medreserve.com","password":"password123"}'

# Test protected endpoint
curl -X GET http://localhost:8080/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Test health endpoint
curl http://localhost:8080/actuator/health
```

## 🐳 Docker Deployment

### Build Docker Image
```bash
# Build the image
docker build -t medreserve-backend .

# Build with specific tag
docker build -t medreserve-backend:v1.0.0 .

# Build for production
docker build --target production -t medreserve-backend:prod .
```

### Run with Docker
```bash
# Run with environment variables
docker run -d \
  --name medreserve-backend \
  -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/medreserve_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=password \
  -e JWT_SECRET=your_jwt_secret_key \
  medreserve-backend

# Run with environment file
docker run -d \
  --name medreserve-backend \
  -p 8080:8080 \
  --env-file .env \
  medreserve-backend

# Run with volume for file uploads
docker run -d \
  --name medreserve-backend \
  -p 8080:8080 \
  -v $(pwd)/uploads:/app/uploads \
  --env-file .env \
  medreserve-backend
```

### Docker Compose
```yaml
# docker-compose.yml
version: '3.8'
services:
  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_URL=jdbc:postgresql://db:5432/medreserve_db
      - DB_USERNAME=postgres
      - DB_PASSWORD=password
    depends_on:
      - db
    volumes:
      - ./uploads:/app/uploads

  db:
    image: postgres:13
    environment:
      - POSTGRES_DB=medreserve_db
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgres_data:
```

```bash
# Start with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f backend

# Stop services
docker-compose down
```

## 🚀 Production Deployment

### Render Platform
1. **Connect Repository**: Link your GitHub repository to Render
2. **Configure Service**: Use the provided `render.yaml` configuration
3. **Set Environment Variables**: Configure in Render dashboard
4. **Deploy**: Automatic deployment on git push

```yaml
# render.yaml
services:
  - type: web
    name: medreserve-backend
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -jar target/medreserve-backend-1.0.0.jar
    envVars:
      - key: DB_URL
        value: your_database_url
      - key: JWT_SECRET
        generateValue: true
```

### AWS Deployment
```bash
# Build for AWS
mvn clean package -DskipTests

# Deploy to Elastic Beanstalk
eb init medreserve-backend
eb create production
eb deploy
```

### Heroku Deployment
```bash
# Create Heroku app
heroku create medreserve-backend

# Set environment variables
heroku config:set DB_URL=your_database_url
heroku config:set JWT_SECRET=your_jwt_secret

# Deploy
git push heroku main
```

## API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/signout` - User logout

### Health Check
- `GET /actuator/health` - Application health status

## 📁 Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/medreserve/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── WebConfig.java
│   │   │   │   ├── DatabaseConfig.java
│   │   │   │   └── SwaggerConfig.java
│   │   │   ├── controller/          # REST controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── DoctorController.java
│   │   │   │   ├── AppointmentController.java
│   │   │   │   └── MedicalRecordController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── request/         # Request DTOs
│   │   │   │   ├── response/        # Response DTOs
│   │   │   │   └── mapper/          # DTO mappers
│   │   │   ├── entity/              # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Doctor.java
│   │   │   │   ├── Patient.java
│   │   │   │   ├── Appointment.java
│   │   │   │   └── MedicalRecord.java
│   │   │   ├── repository/          # Data repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── DoctorRepository.java
│   │   │   │   └── AppointmentRepository.java
│   │   │   ├── security/            # Security components
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   ├── service/             # Business logic
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── DoctorService.java
│   │   │   │   └── AppointmentService.java
│   │   │   ├── exception/           # Exception handling
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── CustomExceptions.java
│   │   │   └── util/                # Utility classes
│   │   │       ├── DateUtils.java
│   │   │       └── ValidationUtils.java
│   │   └── resources/
│   │       ├── application.yml      # Main configuration
│   │       ├── application-dev.yml  # Development config
│   │       ├── application-prod.yml # Production config
│   │       └── data.sql            # Initial data
│   └── test/                        # Test files
│       ├── java/com/medreserve/
│       │   ├── controller/          # Controller tests
│       │   ├── service/             # Service tests
│       │   ├── repository/          # Repository tests
│       │   └── integration/         # Integration tests
│       └── resources/
│           └── application-test.yml # Test configuration
├── ml/                              # ML service (Python)
├── chatbot/                         # Chatbot service (Spring Boot)
├── scripts/                         # Deployment scripts
├── uploads/                         # File upload directory
├── Dockerfile                       # Docker configuration
├── docker-compose.yml              # Docker Compose setup
├── render.yaml                      # Render deployment config
├── pom.xml                         # Maven dependencies
└── README.md                       # This file
```

## 🛠️ Development Guide

### Adding New Features

1. **Create Entity**
   ```java
   @Entity
   @Table(name = "your_entity")
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class YourEntity {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       // Add fields
   }
   ```

2. **Create Repository**
   ```java
   @Repository
   public interface YourEntityRepository extends JpaRepository<YourEntity, Long> {
       // Add custom queries
   }
   ```

3. **Implement Service**
   ```java
   @Service
   @Transactional
   public class YourEntityService {
       // Implement business logic
   }
   ```

4. **Create Controller**
   ```java
   @RestController
   @RequestMapping("/api/your-entity")
   @Validated
   public class YourEntityController {
       // Implement REST endpoints
   }
   ```

5. **Add DTOs and Tests**

### Code Style Guidelines
- **Use Lombok** for reducing boilerplate code
- **Follow Spring Boot conventions** and best practices
- **Add proper validation** annotations (@Valid, @NotNull, etc.)
- **Include comprehensive tests** for all components
- **Use meaningful variable names** and proper documentation
- **Follow RESTful API design** principles
- **Implement proper error handling** and logging

### Database Migrations
```bash
# Create new migration
mvn flyway:migrate

# Check migration status
mvn flyway:info

# Repair migrations
mvn flyway:repair
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Database Connection Failed
```bash
# Check database status
sudo systemctl status postgresql

# Verify connection
psql -h localhost -U postgres -d medreserve_db

# Check configuration
grep -r "DB_URL" src/main/resources/
```

#### 2. JWT Token Issues
```bash
# Verify JWT secret length (minimum 256 bits)
echo $JWT_SECRET | wc -c

# Check token expiration
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/auth/me
```

#### 3. Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080
netstat -tulpn | grep 8080

# Kill process
kill -9 PID

# Change port in configuration
export SERVER_PORT=8081
```

#### 4. Maven Build Issues
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests

# Check dependencies
mvn dependency:tree
```

#### 5. Memory Issues
```bash
# Increase JVM memory
export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=512m"

# For Docker
docker run -m 2g medreserve-backend
```

### Logging Configuration

Application logs are written to:
- **Console** (development mode)
- **File**: `logs/medreserve.log` (production)
- **JSON Format** for structured logging

```yaml
# application.yml
logging:
  level:
    com.medreserve: DEBUG
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### Performance Monitoring
```bash
# Check application metrics
curl http://localhost:8080/actuator/metrics

# Monitor JVM
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Database connection pool
curl http://localhost:8080/actuator/metrics/hikaricp.connections
```

## 🚧 Development Roadmap

### Completed Features ✅
- [x] Authentication & Authorization System
- [x] User Management (Patients, Doctors, Admins)
- [x] JWT-based Security
- [x] Role-based Access Control
- [x] API Documentation (Swagger)
- [x] Database Integration (PostgreSQL/H2)
- [x] Docker Support
- [x] Health Monitoring
- [x] CORS Configuration
- [x] File Upload Support

### In Progress 🚧
- [ ] Appointment Management System
- [ ] Medical Records Management
- [ ] AI Service Integration
- [ ] Real-time Notifications

### Planned Features 📋
- [ ] WebSocket Support for Real-time Chat
- [ ] Advanced Analytics Dashboard
- [ ] Email Notification System
- [ ] Audit Logging
- [ ] Rate Limiting
- [ ] API Versioning
- [ ] Caching Layer (Redis)
- [ ] Message Queue Integration
- [ ] Advanced Search Functionality
- [ ] Reporting System

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup for Contributors
```bash
# Clone your fork
git clone https://github.com/your-username/MedReserve.git
cd MedReserve/backend

# Add upstream remote
git remote add upstream https://github.com/original-repo/MedReserve.git

# Create development branch
git checkout -b develop

# Install pre-commit hooks
mvn clean compile
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## 🆘 Support

- **Documentation**: Check this README and API documentation
- **Issues**: Create an issue on GitHub
- **Email**: support@medreserve.com
- **Discord**: Join our development community

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- JWT.io for authentication standards
- All contributors and testers
