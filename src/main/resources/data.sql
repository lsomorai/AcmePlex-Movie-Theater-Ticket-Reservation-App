-- Sample data for AcmePlex Movie Theater

-- Users
INSERT INTO users (id, email, expiration_date, password, username, usertype) VALUES
(1, 'guest@acmeplex.com', '2124-11-27 20:44:28', 'password123', 'guest', 'GUEST'),
(2, 'admin@acmeplex.com', '2025-11-27 20:44:28', 'admin123', 'admin', 'ADMIN'),
(3, 'test@acmeplex.com', '2025-11-27 20:44:28', 'test123', 'test', 'REGULAR');

-- Theaters
INSERT INTO theaters (id, name) VALUES
(1, 'Scotiabank Chinook'),
(2, 'Cineplex Crowfoot'),
(3, 'Landmark Country Hills');

-- Movies
INSERT INTO movies (id, status, title) VALUES
(1, 'NOW_SHOWING', 'Iron Man'),
(2, 'NOW_SHOWING', 'Avatar'),
(3, 'COMING_SOON', 'Dune');

-- Showtimes (using future dates)
INSERT INTO showtimes (id, date, session, movie_id, theater_id) VALUES
(1, '2026-02-01', 1, 1, 1),
(2, '2026-02-01', 2, 1, 1),
(3, '2026-02-01', 1, 1, 2),
(4, '2026-02-01', 2, 1, 2),
(5, '2026-02-01', 1, 1, 3),
(6, '2026-02-01', 2, 1, 3),
(7, '2026-02-01', 1, 2, 1),
(8, '2026-02-01', 2, 2, 1),
(9, '2026-02-01', 1, 2, 2),
(10, '2026-02-01', 2, 2, 2),
(11, '2026-02-01', 1, 2, 3),
(12, '2026-02-01', 2, 2, 3),
(13, '2026-02-05', 1, 1, 1),
(14, '2026-02-05', 2, 1, 1),
(15, '2026-02-05', 1, 1, 2),
(16, '2026-02-05', 2, 1, 2),
(17, '2026-02-05', 1, 1, 3),
(18, '2026-02-05', 2, 1, 3),
(19, '2026-02-05', 1, 2, 1),
(20, '2026-02-05', 2, 2, 1),
(21, '2026-02-05', 1, 2, 2),
(22, '2026-02-05', 2, 2, 2),
(23, '2026-02-05', 1, 2, 3),
(24, '2026-02-05', 2, 2, 3),
(25, '2026-02-05', 1, 3, 1),
(26, '2026-02-05', 2, 3, 1),
(27, '2026-02-05', 1, 3, 2),
(28, '2026-02-05', 2, 3, 2),
(29, '2026-02-05', 1, 3, 3),
(30, '2026-02-05', 2, 3, 3);

-- Seats for showtime 1 (Iron Man at Scotiabank Chinook, Morning)
INSERT INTO seats (id, price, seat_number, seat_row, seat_type, status, theater_id, showtime_id) VALUES
(1, 15.0, 1, 'A', 'REGULAR', 'AVAILABLE', 1, 1),
(2, 15.0, 2, 'A', 'REGULAR', 'AVAILABLE', 1, 1),
(3, 15.0, 3, 'A', 'REGULAR', 'AVAILABLE', 1, 1),
(4, 15.0, 4, 'A', 'REGULAR', 'AVAILABLE', 1, 1),
(5, 15.0, 5, 'A', 'REGULAR', 'AVAILABLE', 1, 1),
(6, 15.0, 1, 'B', 'REGULAR', 'AVAILABLE', 1, 1),
(7, 15.0, 2, 'B', 'REGULAR', 'AVAILABLE', 1, 1),
(8, 15.0, 3, 'B', 'REGULAR', 'AVAILABLE', 1, 1),
(9, 15.0, 4, 'B', 'REGULAR', 'AVAILABLE', 1, 1),
(10, 15.0, 5, 'B', 'REGULAR', 'AVAILABLE', 1, 1),
(11, 20.0, 1, 'C', 'MEMBER', 'AVAILABLE', 1, 1),
(12, 20.0, 2, 'C', 'MEMBER', 'AVAILABLE', 1, 1),
(13, 20.0, 3, 'C', 'MEMBER', 'AVAILABLE', 1, 1),
(14, 20.0, 4, 'C', 'MEMBER', 'AVAILABLE', 1, 1),
(15, 20.0, 5, 'C', 'MEMBER', 'AVAILABLE', 1, 1),
(16, 20.0, 1, 'D', 'MEMBER', 'AVAILABLE', 1, 1),
(17, 20.0, 2, 'D', 'MEMBER', 'AVAILABLE', 1, 1),
(18, 20.0, 3, 'D', 'MEMBER', 'AVAILABLE', 1, 1),
(19, 20.0, 4, 'D', 'MEMBER', 'AVAILABLE', 1, 1),
(20, 20.0, 5, 'D', 'MEMBER', 'AVAILABLE', 1, 1);

-- Seats for showtime 2 (Iron Man at Scotiabank Chinook, Afternoon)
INSERT INTO seats (id, price, seat_number, seat_row, seat_type, status, theater_id, showtime_id) VALUES
(21, 15.0, 1, 'A', 'REGULAR', 'AVAILABLE', 1, 2),
(22, 15.0, 2, 'A', 'REGULAR', 'AVAILABLE', 1, 2),
(23, 15.0, 3, 'A', 'REGULAR', 'AVAILABLE', 1, 2),
(24, 15.0, 4, 'A', 'REGULAR', 'AVAILABLE', 1, 2),
(25, 15.0, 5, 'A', 'REGULAR', 'AVAILABLE', 1, 2),
(26, 15.0, 1, 'B', 'REGULAR', 'AVAILABLE', 1, 2),
(27, 15.0, 2, 'B', 'REGULAR', 'AVAILABLE', 1, 2),
(28, 15.0, 3, 'B', 'REGULAR', 'AVAILABLE', 1, 2),
(29, 15.0, 4, 'B', 'REGULAR', 'AVAILABLE', 1, 2),
(30, 15.0, 5, 'B', 'REGULAR', 'AVAILABLE', 1, 2),
(31, 20.0, 1, 'C', 'MEMBER', 'AVAILABLE', 1, 2),
(32, 20.0, 2, 'C', 'MEMBER', 'AVAILABLE', 1, 2),
(33, 20.0, 3, 'C', 'MEMBER', 'AVAILABLE', 1, 2),
(34, 20.0, 4, 'C', 'MEMBER', 'AVAILABLE', 1, 2),
(35, 20.0, 5, 'C', 'MEMBER', 'AVAILABLE', 1, 2),
(36, 20.0, 1, 'D', 'MEMBER', 'AVAILABLE', 1, 2),
(37, 20.0, 2, 'D', 'MEMBER', 'AVAILABLE', 1, 2),
(38, 20.0, 3, 'D', 'MEMBER', 'AVAILABLE', 1, 2),
(39, 20.0, 4, 'D', 'MEMBER', 'AVAILABLE', 1, 2),
(40, 20.0, 5, 'D', 'MEMBER', 'AVAILABLE', 1, 2);

-- Seats for showtime 7 (Avatar at Scotiabank Chinook, Morning)
INSERT INTO seats (id, price, seat_number, seat_row, seat_type, status, theater_id, showtime_id) VALUES
(41, 15.0, 1, 'A', 'REGULAR', 'AVAILABLE', 1, 7),
(42, 15.0, 2, 'A', 'REGULAR', 'AVAILABLE', 1, 7),
(43, 15.0, 3, 'A', 'REGULAR', 'AVAILABLE', 1, 7),
(44, 15.0, 4, 'A', 'REGULAR', 'AVAILABLE', 1, 7),
(45, 15.0, 5, 'A', 'REGULAR', 'AVAILABLE', 1, 7),
(46, 15.0, 1, 'B', 'REGULAR', 'AVAILABLE', 1, 7),
(47, 15.0, 2, 'B', 'REGULAR', 'AVAILABLE', 1, 7),
(48, 15.0, 3, 'B', 'REGULAR', 'AVAILABLE', 1, 7),
(49, 15.0, 4, 'B', 'REGULAR', 'AVAILABLE', 1, 7),
(50, 15.0, 5, 'B', 'REGULAR', 'AVAILABLE', 1, 7),
(51, 20.0, 1, 'C', 'MEMBER', 'AVAILABLE', 1, 7),
(52, 20.0, 2, 'C', 'MEMBER', 'AVAILABLE', 1, 7),
(53, 20.0, 3, 'C', 'MEMBER', 'AVAILABLE', 1, 7),
(54, 20.0, 4, 'C', 'MEMBER', 'AVAILABLE', 1, 7),
(55, 20.0, 5, 'C', 'MEMBER', 'AVAILABLE', 1, 7),
(56, 20.0, 1, 'D', 'MEMBER', 'AVAILABLE', 1, 7),
(57, 20.0, 2, 'D', 'MEMBER', 'AVAILABLE', 1, 7),
(58, 20.0, 3, 'D', 'MEMBER', 'AVAILABLE', 1, 7),
(59, 20.0, 4, 'D', 'MEMBER', 'AVAILABLE', 1, 7),
(60, 20.0, 5, 'D', 'MEMBER', 'AVAILABLE', 1, 7);
