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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        // Get username and userId from session
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("userId");
        
        // If it's a guest user (Ordinary User), set userId to 1
        if (username != null && username.equals("Ordinary User")) {
            userId = 1; // Set guest user ID to 1
        }

        // Get showtime and seats
        Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow();
        List<Seat> seats = Arrays.stream(selectedSeats.split(","))
                               .map(Long::parseLong)
                               .map(id -> seatRepository.findById(id).orElseThrow())
                               .collect(Collectors.toList());
        
        double totalPrice = seats.stream()
                               .mapToDouble(Seat::getPrice)
                               .sum();

        String displayName = username != null ? username : "Ordinary User";
        
        // Add attributes
        model.addAttribute("displayName", displayName);
        model.addAttribute("userId", userId);
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
            String username = (String) session.getAttribute("username");
            
            // If it's a guest user (Ordinary User), set userId to 1
            if (username != null && username.equals("Ordinary User")) {
                userId = 1;
            }
            
            System.out.println("Processing payment for user: " + userId);
            String referenceNumber = ticketPaymentService.processTicketPayment(paymentRequest, userId);
            // Store reference number in session for the success page
            session.setAttribute("lastBookingReference", referenceNumber);
            return "success";
        } catch (Exception e) {
            System.out.println("Payment failed with error: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/booking-success")
    public String showBookingSuccess(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String displayName = username != null ? username : "Ordinary User";
        String referenceNumber = (String) session.getAttribute("lastBookingReference");
        
        model.addAttribute("displayName", displayName);
        model.addAttribute("referenceNumber", referenceNumber);
        
        // Clear the reference number from session after displaying
        session.removeAttribute("lastBookingReference");
        
        return "booking-success";
    }
} 