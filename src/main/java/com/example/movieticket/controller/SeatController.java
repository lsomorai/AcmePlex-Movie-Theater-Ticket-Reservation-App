package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.movieticket.entity.Seat;
import com.example.movieticket.repository.SeatRepository;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

@Controller
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @GetMapping("/showtimes/{showtimeId}/seats")
    public String showSeats(@PathVariable Long showtimeId, Model model) {
        List<Seat> seats = seatRepository.findByShowtimeId(showtimeId);
        
        Map<String, List<Seat>> seatsByRow = new TreeMap<>();
        for (Seat seat : seats) {
            seatsByRow.computeIfAbsent(seat.getSeatRow(), k -> new ArrayList<>())
                     .add(seat);
        }
        
        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("seatsByRow", seatsByRow);
        return "seats";
    }
} 