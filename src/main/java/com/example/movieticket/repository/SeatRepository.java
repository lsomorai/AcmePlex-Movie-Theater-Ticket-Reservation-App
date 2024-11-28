/*
 * SeatRepository.java
 * Author: Lucien Somorai
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movieticket.entity.Seat;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowtimeId(Long showtimeId);
} 