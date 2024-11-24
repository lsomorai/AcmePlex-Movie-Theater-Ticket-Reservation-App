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
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class TicketPaymentController {

    @Autowired
    private TicketPaymentService ticketPaymentService;
    
    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @Autowired
    private SeatRepository seatRepository;
    
    @Autowired
    private NameRepository nameRepository;
    
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditRepository creditRepository;

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
        
        // Add payment info for registered users
        if (username != null && !username.equals("Ordinary User")) {
            // Get user's name
            Name userName = nameRepository.findByUsername(username);
            if (userName != null) {
                model.addAttribute("userFullName", userName.getFirst() + " " + userName.getLast());
            }
            
            // Get user's latest card
            List<Card> userCards = cardRepository.findByUsername(username);
            if (!userCards.isEmpty()) {
                Card latestCard = userCards.get(0); // Get the first card
                model.addAttribute("userCard", latestCard);
            }
        }
        
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
        
        // Add email information for registered users
        if (username != null && !username.equals("Ordinary User")) {
            List<User> users = userRepository.findByUsername(username);
            if (!users.isEmpty()) {
                String userEmail = users.get(0).getEmail();
                model.addAttribute("userEmail", userEmail);
            }
        }
        
        // Clear the reference number from session after displaying
        session.removeAttribute("lastBookingReference");
        
        return "booking-success";
    }

    @PostMapping("/api/send-receipt")
    @ResponseBody
    public Map<String, Object> sendReceiptEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String referenceNumber = request.get("referenceNumber");
        
        try {
            // Here you would implement your email sending logic
            // For now, we'll just return success
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }

    @PostMapping("/api/validate-credit")
    @ResponseBody
    public Map<String, Object> validateCredit(@RequestBody Map<String, String> request) {
        String creditCode = request.get("creditCode");
        Map<String, Object> response = new HashMap<>();
        
        Optional<Credit> credit = creditRepository.findByCreditCodeAndStatusAndExpiryDateGreaterThanEqual(
            creditCode, 
            CreditStatus.UNUSED, 
            LocalDate.now()
        );
        
        if (credit.isPresent()) {
            response.put("valid", true);
            response.put("amount", credit.get().getAmount());
        } else {
            response.put("valid", false);
            response.put("message", "Invalid or expired credit code");
        }
        
        return response;
    }
} 