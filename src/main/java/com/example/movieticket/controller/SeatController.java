package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/seats")
    public String showSeats(Model model) {
        // Hard-coded theater and session for now
        int theaterId = 1;
        int sessionId = 1;
        
        List<Seat> seats = seatRepository.findByTheaterIdAndSession(theaterId, sessionId);
        
        // Organize seats by row for easier display
        Map<String, List<Seat>> seatsByRow = new TreeMap<>();
        for (Seat seat : seats) {
            seatsByRow.computeIfAbsent(seat.getSeatRow(), k -> new ArrayList<>())
                     .add(seat);
        }
        
        model.addAttribute("seatsByRow", seatsByRow);
        return "seats";
    }
} 