/*
 * MovieRepository.java
 * Author: Lucien Somorai
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.repository;

import com.example.movieticket.entity.Movie;
import com.example.movieticket.entity.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT DISTINCT s.movie FROM Showtime s WHERE s.theatre.id = :theatreId ORDER BY s.movie.status ASC")
    List<Movie> findMoviesByTheatreId(@Param("theatreId") Long theatreId);

    @Query("SELECT m FROM Movie m WHERE m.status = :status")
    List<Movie> findByStatus(@Param("status") MovieStatus status);

    List<Movie> findByTitleContainingIgnoreCaseAndStatus(String title, MovieStatus status);
} 