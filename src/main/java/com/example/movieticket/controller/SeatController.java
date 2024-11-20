package com.example.movieticket.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieticket.model.Seat;
import com.example.movieticket.model.Showtime;
import com.example.movieticket.repository.ShowtimeRepository;
import com.example.movieticket.service.SeatService;

@RestController
@RequestMapping(path = "/seat")
public class SeatController {

    private final SeatService seatService;
    private final ShowtimeRepository showtimeRepository;

    public SeatController(SeatService seatService, ShowtimeRepository showtimeRepository) {
        this.seatService = seatService;
        this.showtimeRepository = showtimeRepository;
    }

    @GetMapping("/available/{showtimeId}")
    public List<Seat> getAvailableSeats(@PathVariable Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found"));
        return seatService.getAvailableSeatsByShowtime(showtime);
    }
}