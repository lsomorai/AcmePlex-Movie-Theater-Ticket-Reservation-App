package com.example.movieticket.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.movieticket.model.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    public interface ShowtimeBrief {
        Long getId();
        LocalDate getDate();
        LocalTime getTime();
    }

    @Query("SELECT s.id AS id, s.date AS date, s.time AS time " +
           "FROM Showtime s WHERE s.movie.id = :movieId")
    List<ShowtimeBrief> findShowtimeByMovieId(Long movieId);

    @Query("SELECT s.id AS id, s.date AS date, s.time AS time " +
           "FROM Showtime s WHERE s.movie.id = :movieId AND s.theatre.id = :theatreId")
    List<ShowtimeBrief> getShowtimesForMovieAndTheater(Long movieId, Long theatreId);
}
