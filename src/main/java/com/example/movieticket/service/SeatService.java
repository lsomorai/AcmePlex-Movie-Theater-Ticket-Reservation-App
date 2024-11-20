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
}