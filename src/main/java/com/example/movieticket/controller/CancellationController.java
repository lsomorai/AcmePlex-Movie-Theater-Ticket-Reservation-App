package com.example.movieticket.controller;

import com.example.movieticket.service.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.movieticket.exception.TicketNotRefundableException;
import com.example.movieticket.exception.TicketNotFoundException;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

@Controller  // For serving HTML pages
@RequestMapping("/cancellation")  // Base URL for cancellation
@RequiredArgsConstructor
public class CancellationController {

    private final CancellationService cancellationService;

    // This is the GET method to show the cancellation page
    @GetMapping("/cancel-ticket")
    public String showCancellationPage(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String displayName = username != null ? username : "Ordinary User";
        model.addAttribute("displayName", displayName);
        return "cancellation";
    }

    // This is the POST method to handle the cancellation request
    @PostMapping("/cancel-booking")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelTicket(@RequestBody Map<String, String> requestBody) {
        // Extract booking reference number from the request
        String bookingReference = requestBody.get("bookingReference");

        // Validate the booking reference
        if (bookingReference == null || bookingReference.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Booking reference is required."
            ));
        }

        try {
            String resultMessage = cancellationService.cancelTicket(bookingReference);

            boolean isSuccess = !resultMessage.toLowerCase().contains("failed") && !resultMessage.toLowerCase().contains("error");

            Map<String, Object> response = new HashMap<>();
            response.put("success", isSuccess);
            response.put("message", resultMessage);

            return ResponseEntity.ok(response);
        } catch (TicketNotRefundableException e) {
            return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (TicketNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error occurred: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/search-ticket")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchTicket(@RequestBody Map<String, String> requestBody, 
                                                            HttpSession session) {
        String bookingReference = requestBody.get("bookingReference");
        Integer currentUserId = (Integer) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        
        // For guest user (Ordinary User), set userId to 1
        if (username != null && username.equals("Ordinary User")) {
            currentUserId = 1;
        }

        try {
            // Get ticket details from service
            Map<String, Object> ticketDetails = cancellationService.getTicketDetails(bookingReference, currentUserId);
            return ResponseEntity.ok(ticketDetails);
        } catch (TicketNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", "Ticket not found with this reference number."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }
}