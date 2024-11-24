package com.example.movieticket.repository;

import com.example.movieticket.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("SELECT s FROM Showtime s WHERE s.theatre.id = :theatreId " +
           "AND s.movie.id = :movieId " +
           "AND (s.date > CURRENT_DATE OR " +
           "(s.date = CURRENT_DATE AND " +
           "((s.session = 1 AND HOUR(CURRENT_TIME) < 10) OR " +
           "(s.session = 2 AND HOUR(CURRENT_TIME) < 14) OR " +
           "(s.session = 3 AND HOUR(CURRENT_TIME) < 19))))" +
           "ORDER BY s.date, s.session")
    List<Showtime> findByTheatreIdAndMovieId(@Param("theatreId") Long theatreId, 
                                            @Param("movieId") Long movieId);
} 