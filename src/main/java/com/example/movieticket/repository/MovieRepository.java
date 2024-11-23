package com.example.movieticket.repository;

import com.example.movieticket.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT DISTINCT s.movie FROM Showtime s WHERE s.theatre.id = :theatreId ORDER BY s.movie.status ASC")
    List<Movie> findMoviesByTheatreId(@Param("theatreId") Long theatreId);
} 