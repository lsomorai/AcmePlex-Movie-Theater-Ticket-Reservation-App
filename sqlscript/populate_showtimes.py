from datetime import datetime, timedelta

def generate_showtimes_sql():
    # Configuration
    dates = [
        datetime.now() + timedelta(days=2),  # 2 days from now
        datetime.now() + timedelta(days=7),  # 7 days from now
        datetime.now() + timedelta(days=8)   # 8 days from now
    ]
    sessions = [1, 2]  # morning, afternoon only
    movie_ids = [1, 2, 3]  # Iron Man, Avatar, Dune
    theatre_ids = [1, 2, 3]  # Scotiabank Chinook, Cineplex Crowfoot, Landmark Country Hills

    # SQL file header
    sql = "USE acmeplex;\n\n"
    sql += f"-- Inserting showtimes for {dates[0].strftime('%B %d')}, "
    sql += f"{dates[1].strftime('%B %d')}, and {dates[2].strftime('%B %d, %Y')}\n"
    sql += "INSERT INTO showtimes (movie_id, theater_id, date, session) VALUES\n"

    # Generate values
    values = []
    for current_date in dates:
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