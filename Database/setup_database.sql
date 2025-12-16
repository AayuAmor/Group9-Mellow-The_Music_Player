-- Complete Database Setup Script for Mellow Application
-- Run this script in MySQL Workbench (BY DADA)
DROP DATABASE IF EXISTS UserData;
CREATE DATABASE UserData;
USE UserData;

-- Create users table with all required columns
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert test admin user
INSERT INTO users (username, email, password, role) 
VALUES ('admin', 'admin@mellow.com', 'admin123', 'admin');

-- Insert test regular user
INSERT INTO users (username, email, password, role) 
VALUES ('testuser', 'user@mellow.com', 'user123', 'user');

-- Verify table structure
DESCRIBE users;

-- Show all users
SELECT user_id, username, email, role, created_at FROM users;

-- Success message
SELECT 'Database setup completed successfully!' AS Status;
