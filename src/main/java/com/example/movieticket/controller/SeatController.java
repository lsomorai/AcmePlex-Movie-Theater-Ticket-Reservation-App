package com.example.movieticket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/reserve")
    public ResponseEntity<String> reserveSeat(@RequestBody ReserveSeatRequest reserveRequest) {
        try {
            seatService.reserveSeat(reserveRequest.getSeatId());
            return ResponseEntity.ok("Seat reserved successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/cancel")
    public ResponseEntity<String> cancelSeatReservation(@RequestBody CancelSeatRequest cancelRequest) {
        try {
            seatService.cancelReservation(cancelRequest.getSeatId());
            return ResponseEntity.ok("Reservation canceled successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

class ReserveSeatRequest {
    private Long seatId;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }
}

class CancelSeatRequest {
    private Long seatId;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }
}