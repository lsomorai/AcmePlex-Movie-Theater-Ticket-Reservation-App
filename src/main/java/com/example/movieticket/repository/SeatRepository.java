package com.example.movieticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.movieticket.model.Seat;
import com.example.movieticket.model.Showtime;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s WHERE s.showtime = :showtime AND s.status = 'AVAILABLE'")
    List<Seat> findAvailableSeatsByShowtime(@Param("showtime") Showtime showtime);
}