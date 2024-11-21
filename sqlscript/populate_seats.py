def generate_seats_sql():
    # Configuration
    rows = ['A', 'B', 'C', 'D', 'E']
    seats_per_row = 10
    price = 15.00
    total_showtimes = 81  # From your showtimes table
    
    # SQL file header
    sql = "USE qbtppw7yhyi4ttyc;\n\n"
    sql += "-- Inserting seats for all showtimes\n"
    sql += "INSERT INTO seats (theater_id, showtime_id, seat_row, seat_number, seat_type, price, status) VALUES\n"
    
    values = []
    # For each showtime
    for showtime_id in range(1, total_showtimes + 1):
        # Calculate theater_id based on showtime_id pattern (1-3 repeating)
        theater_id = ((showtime_id - 1) // 3 % 3) + 1
        
        # For each row
        for row in rows:
            # For each seat in row
            for seat_num in range(1, seats_per_row + 1):
                # Determine seat type
                seat_type = "special" if row == 'A' and seat_num <= 5 else "regular"
                
                values.append(
                    f"({theater_id}, {showtime_id}, '{row}', {seat_num}, '{seat_type}', {price:.2f}, 'AVAILABLE')"
                )
    
    # Join values with commas and add semicolon at the end
    sql += ",\n".join(values) + ";"
    
    return sql

# Generate and save to file
with open('generate_seats.sql', 'w') as f:
    f.write(generate_seats_sql())

print("SQL script generated successfully!")