from datetime import datetime, timedelta

def generate_showtimes_sql():
    # Configuration
    start_date = datetime(2025, 2, 1)
    num_days = 3
    sessions = [1, 2, 3]  # morning, afternoon, evening
    movie_ids = [1, 2, 3]  # Iron Man, Avatar, Dune
    theatre_ids = [1, 2, 3]  # Scotiabank Chinook, Cineplex Crowfoot, Landmark Country Hills

    # SQL file header
    sql = "USE qbtppw7yhyi4ttyc;\n\n"
    sql += "-- Inserting showtimes for Feb 1-3, 2025\n"
    sql += "INSERT INTO showtimes (movie_id, theater_id, date, session) VALUES\n"

    # Generate values
    values = []
    for day in range(num_days):
        current_date = start_date + timedelta(days=day)
        formatted_date = current_date.strftime('%Y-%m-%d')
        
        for movie_id in movie_ids:
            for theatre_id in theatre_ids:
                for session in sessions:
                    values.append(
                        f"({movie_id}, {theatre_id}, '{formatted_date}', {session})"
                    )

    # Join values with commas and add semicolon at the end
    sql += ",\n".join(values) + ";"

    return sql

# Generate and save to file
with open('generate_showtimes.sql', 'w') as f:
    f.write(generate_showtimes_sql())

print("SQL script generated successfully!")