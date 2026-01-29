-- V1__init_schema.sql
-- Initial database schema for AcmePlex Movie Theater

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    usertype VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    expiration_date TIMESTAMP
);

-- Names table (for user full names)
CREATE TABLE IF NOT EXISTS names (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first VARCHAR(50) NOT NULL,
    last VARCHAR(50) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Theaters table
CREATE TABLE IF NOT EXISTS theaters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Movies table
CREATE TABLE IF NOT EXISTS movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NOW_SHOWING'
);

-- Showtimes table
CREATE TABLE IF NOT EXISTS showtimes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    session INT NOT NULL,
    movie_id BIGINT NOT NULL,
    theater_id BIGINT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (theater_id) REFERENCES theaters(id)
);

-- Seats table
CREATE TABLE IF NOT EXISTS seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    theater_id INT NOT NULL,
    showtime_id BIGINT NOT NULL,
    seat_row VARCHAR(5) NOT NULL,
    seat_number INT NOT NULL,
    seat_type VARCHAR(20),
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id)
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    paymentid BIGINT AUTO_INCREMENT PRIMARY KEY,
    cardnumber VARCHAR(20),
    cardname VARCHAR(100),
    expirydate VARCHAR(10),
    cvv VARCHAR(4),
    amount DECIMAL(10,2) NOT NULL,
    userid INT,
    note VARCHAR(100),
    FOREIGN KEY (userid) REFERENCES users(id)
);

-- Cards table (for saved user cards)
CREATE TABLE IF NOT EXISTS cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(20) NOT NULL,
    expiry_date VARCHAR(10),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tickets table
CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    movie_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    paymentid BIGINT,
    purchase_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    isRefundable BOOLEAN NOT NULL DEFAULT TRUE,
    reference_number VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id),
    FOREIGN KEY (paymentid) REFERENCES payments(paymentid)
);

-- Credits table (for refund credits)
CREATE TABLE IF NOT EXISTS credits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    credit_code VARCHAR(50) UNIQUE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    expiry_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNUSED'
);

-- Create indexes for better query performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_tickets_reference ON tickets(reference_number);
CREATE INDEX idx_tickets_user ON tickets(user_id);
CREATE INDEX idx_seats_showtime ON seats(showtime_id);
CREATE INDEX idx_showtimes_movie ON showtimes(movie_id);
CREATE INDEX idx_showtimes_theater ON showtimes(theater_id);
CREATE INDEX idx_credits_code ON credits(credit_code);
