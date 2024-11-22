package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.movieticket.service.TicketPaymentService;
import com.example.movieticket.entity.*;
import com.example.movieticket.repository.*;
import com.example.movieticket.dto.PaymentRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpSession;

@Controller
public class TicketPaymentController {

    @Autowired
    private TicketPaymentService ticketPaymentService;
    
    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @Autowired
    private SeatRepository seatRepository;

    @GetMapping("/ticket-payment")
    public String showPaymentPage(@RequestParam Long showtimeId,
                                @RequestParam String selectedSeats,
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

        // Get username from session
        String username = (String) session.getAttribute("username");
        String displayName = username != null ? username : "Ordinary User";
        
        // Add attributes
        model.addAttribute("displayName", displayName);
        model.addAttribute("showtime", showtime);
        model.addAttribute("seats", seats);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("selectedSeatsString", selectedSeats);
        
        return "ticket-payment";
    }

    @PostMapping("/process-ticket-payment")
    @ResponseBody
    public String processPayment(@RequestBody PaymentRequest paymentRequest,
                               HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            ticketPaymentService.processTicketPayment(paymentRequest, userId);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }
} 