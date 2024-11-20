package com.example.movieticket;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.movieticket.model.Movie;
import com.example.movieticket.model.Seat;
import com.example.movieticket.model.Showtime;
import com.example.movieticket.model.Theatre;
import com.example.movieticket.repository.MovieRepository;
import com.example.movieticket.repository.SeatRepository;
import com.example.movieticket.repository.ShowtimeRepository;
import com.example.movieticket.repository.TheatreRepository;

@SpringBootApplication
public class MovieTheatreApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieTheatreApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(
            MovieRepository movieRepository,
            TheatreRepository theatreRepository,
            ShowtimeRepository showtimeRepository,
            SeatRepository seatRepository) {
        return (args) -> {
            Movie movie1 = movieRepository.save(new Movie("Movie 1"));
            Movie movie2 = movieRepository.save(new Movie("Movie 2"));
            Movie movie3 = movieRepository.save(new Movie("Movie 3"));
            Movie movie4 = movieRepository.save(new Movie("Movie 4"));

            Theatre firstTheater = theatreRepository.save(new Theatre("Theatre 1"));
            Theatre secondTheater = theatreRepository.save(new Theatre("Theatre 2"));

            Movie[] movies = {movie1, movie2, movie3, movie4};
            Theatre[] theatres = {firstTheater, secondTheater};
            LocalDate date;

            for (int movieIndex = 0; movieIndex < movies.length; movieIndex++) {
                Movie movie = movies[movieIndex];
                Theatre theatre = theatres[movieIndex % theatres.length]; 

                for (int dayOffset = 0; dayOffset < 3; dayOffset++) {
                    if (movieIndex != 2) {
                        date = LocalDate.of(2022, 12, 14 + dayOffset);
                    } else {
                        date = LocalDate.of(2001, 8, 3 + dayOffset);
                    }

                    for (int hour = 19; hour <= 23; hour++) {
                        Showtime firstShowtime = showtimeRepository.save(
                                new Showtime(movie, theatre, date, LocalTime.of(hour, 0)));
                        Showtime secondShowtime = showtimeRepository.save(
                                new Showtime(movie, theatre, date, LocalTime.of(hour, 30)));

                        populateSeats(firstShowtime, seatRepository);
                        populateSeats(secondShowtime, seatRepository);
                    }
                }
            }
        };
    }

    private void populateSeats(Showtime showtime, SeatRepository seatRepository) {
        String[] rows = {"A", "B", "C", "D", "E"};
        int seatsPerRow = 10;

        for (String row : rows) {
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                String seatType = (row.equals("A") || row.equals("B")) ? "VIP" : "Regular";
                double price = seatType.equals("VIP") ? 20.0 : 12.0;

                Seat seat = new Seat(showtime, row, seatNumber, seatType, price, "AVAILABLE");
                seatRepository.save(seat);
            }
        }
    }
}
