package com.example.movieticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.movieticket.model.Seat;
import com.example.movieticket.model.Showtime;
import com.example.movieticket.repository.SeatRepository;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getAvailableSeatsByShowtime(Showtime showtime) {
        return seatRepository.findAvailableSeatsByShowtime(showtime);
    }

    public void reserveSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        if (!"AVAILABLE".equalsIgnoreCase(seat.getStatus())) {
            throw new IllegalArgumentException("Seat is not available for reservation");
        }

        seat.setStatus("RESERVED");
        seatRepository.save(seat);
    }

    public void cancelReservation(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));
    
        if (!"RESERVED".equalsIgnoreCase(seat.getStatus())) {
            throw new IllegalArgumentException("Seat is not currently reserved");
        }
    
        seat.setStatus("AVAILABLE");
        seatRepository.save(seat);
    }
}