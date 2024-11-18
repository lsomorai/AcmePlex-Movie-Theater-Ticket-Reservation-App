theaters = [1, 2, 3]
sessions = [1, 2, 3]
rows = ['A', 'B', 'C', 'D', 'E']
seats = range(1, 11)  # 1 to 10

# Start the SQL statement
sql = "INSERT INTO seats (theater_id, session, seat_row, seat_number, seat_type, price, status) VALUES\n"
values = []

# Generate all combinations
for theater in theaters:
    for session in sessions:
        for row in rows:
            for seat in seats:
                seat_type = 'SPECIAL' if (row == 'A' and seat <= 5) else 'STANDARD'
                value = f"({theater}, {session}, '{row}', {seat}, '{seat_type}', 15.00, 'AVAILABLE')"
                values.append(value)

# Join all values with commas and add semicolon at the end
sql += ",\n".join(values) + ";"

# Write to file
with open('insert_seats.sql', 'w') as f:
    f.write(sql)

print(f"Generated {len(values)} seat entries in 'insert_seats.sql'")