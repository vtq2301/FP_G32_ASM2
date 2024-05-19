CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255),
                       address VARCHAR(255),
                       phone_number VARCHAR(255),
                       policy_holder_id VARCHAR(255)
);
