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
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TicketController {

    @Autowired
    private SeatRepository seatRepository;
    
    @PostMapping("/showtimes/{showtimeId}/book")
    public String showTicketConfirmation(@PathVariable Long showtimeId,
                                       @RequestParam String selectedSeats,
                                       Model model,
                                       HttpSession session) {
        // Get selected seats
        List<Long> seatIds = Arrays.stream(selectedSeats.split(","))
                                  .map(Long::parseLong)
                                  .collect(Collectors.toList());
        List<Seat> seats = seatRepository.findAllById(seatIds);
        
        // Get showtime details (first seat's showtime contains all needed info)
        Showtime showtime = seats.get(0).getShowtime();
        
        // Calculate total price
        double totalPrice = seats.stream()
                               .mapToDouble(Seat::getPrice)
                               .sum();
        
        // Add attributes to model
        model.addAttribute("seats", seats);
        model.addAttribute("showtime", showtime);
        model.addAttribute("totalPrice", totalPrice);
        
        // Add username display
        String username = (String) session.getAttribute("username");
        model.addAttribute("displayName", username != null ? username : "Ordinary User");
        
        return "ticket-confirmation";
    }
} 