package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.movieticket.entity.Seat;
import com.example.movieticket.entity.Showtime;
import com.example.movieticket.repository.SeatRepository;
import com.example.movieticket.repository.ShowtimeRepository;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TicketController {

    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @Autowired
    private SeatRepository seatRepository;
    
    @PostMapping("/ticket-confirmation")
    public String showTicketConfirmation(@RequestParam("selectedSeats") String selectedSeats,
                                       @RequestParam("showtimeId") Long showtimeId,
                                       Model model,
                                       HttpSession session) {
        // Get showtime and seats
        Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow();
        List<Seat> seats = Arrays.stream(selectedSeats.split(","))
                               .map(Long::parseLong)
                               .map(id -> seatRepository.findById(id).orElseThrow())
                               .collect(Collectors.toList());
        
        double totalPrice = seats.stream()
                               .mapToDouble(Seat::getPrice)
                               .sum();

        // Get username from session (matching SeatController)
        String username = (String) session.getAttribute("username");
        String displayName = username != null ? username : "Ordinary User";
        
        // Add attributes to model
        model.addAttribute("displayName", displayName);
        model.addAttribute("showtime", showtime);
        model.addAttribute("seats", seats);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("selectedSeatsString", selectedSeats);
        
        return "ticket-confirmation";
    }
} 