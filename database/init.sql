-- MedReserve AI Database Initialization Script
-- This script will be executed when the MySQL container starts for the first time

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS medreserve;
USE medreserve;

-- Grant privileges to the medreserve user
GRANT ALL PRIVILEGES ON medreserve.* TO 'medreserve'@'%';
FLUSH PRIVILEGES;

-- Note: Tables will be created automatically by Spring Boot JPA/Hibernate
-- when the application starts with spring.jpa.hibernate.ddl-auto=update

-- Optional: Insert some initial data (will be handled by the application)
-- This file is mainly for database and user setup
