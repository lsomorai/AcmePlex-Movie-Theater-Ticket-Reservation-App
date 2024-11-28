INSERT INTO users (username, password, usertype, expiration_date, email) VALUES 
('guest', 'password123', 'GUEST', DATE_ADD(NOW(), INTERVAL 100 YEAR), 'guest@acmeplex.com'),
('admin', 'admin123', 'ADMIN', DATE_ADD(NOW(), INTERVAL 1 YEAR), 'admin@acmeplex.com');