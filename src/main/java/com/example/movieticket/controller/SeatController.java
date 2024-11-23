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
import jakarta.servlet.http.HttpSession;
import com.example.movieticket.entity.Showtime;
import com.example.movieticket.repository.ShowtimeRepository;
import com.example.movieticket.entity.Movie;
import com.example.movieticket.entity.MovieStatus;

@Controller
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @GetMapping("/showtimes/{showtimeId}/seats")
    public String showSeats(@PathVariable Long showtimeId, Model model, HttpSession session) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
            .orElseThrow(() -> new RuntimeException("Showtime not found"));
        Movie movie = showtime.getMovie();
        
        // Get the theatre from showtime
        model.addAttribute("theatre", showtime.getTheatre());
        
        // Store the theatre ID in session if it came from a theatre-specific path
        Long currentTheatreId = (Long) session.getAttribute("currentTheatreId");
        if (currentTheatreId != null) {
            model.addAttribute("fromTheatre", true);
        }
        
        List<Seat> seats = seatRepository.findByShowtimeId(showtimeId);
        
        // Get user status
        String username = (String) session.getAttribute("username");
        boolean isRegisteredUser = username != null && !username.equals("Ordinary User");
        
        // If movie is COMING_SOON, mark non-special seats as unavailable
        if (movie.getStatus() == MovieStatus.COMING_SOON) {
            seats.forEach(seat -> {
                if (!"special".equals(seat.getSeatType()) || !isRegisteredUser) {
                    seat.setStatus("NOT_AVAILABLE");
                }
            });
        }
        
        Map<String, List<Seat>> seatsByRow = new TreeMap<>();
        for (Seat seat : seats) {
            seatsByRow.computeIfAbsent(seat.getSeatRow(), k -> new ArrayList<>())
                     .add(seat);
        }
        
        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("seatsByRow", seatsByRow);
        model.addAttribute("movieStatus", movie.getStatus());
        model.addAttribute("displayName", username != null ? username : "Ordinary User");
        model.addAttribute("isRegisteredUser", isRegisteredUser);
        
        return "seats";
    }
} 