-- MedReserve AI Database Initialization Script
-- This script will be executed when the PostgreSQL container starts for the first time

-- Grant privileges to the user (database already created by Docker)
GRANT ALL PRIVILEGES ON SCHEMA public TO medreserve_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO medreserve_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO medreserve_user;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO medreserve_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO medreserve_user;

-- Note: Tables will be created automatically by Spring Boot JPA/Hibernate
-- when the application starts with spring.jpa.hibernate.ddl-auto=update

-- Optional: Insert some initial data (will be handled by the application)
-- This file is mainly for database and user setup
