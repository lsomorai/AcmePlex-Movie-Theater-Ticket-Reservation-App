package com.example.movieticket.repository;

import com.example.movieticket.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("SELECT s FROM Showtime s WHERE s.theatre.id = :theatreId AND s.movie.id = :movieId ORDER BY s.date, s.session")
    List<Showtime> findByTheatreIdAndMovieId(@Param("theatreId") Long theatreId, @Param("movieId") Long movieId);
} 