package com.example.movieticket.controller;

import com.example.movieticket.service.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.movieticket.exception.TicketNotRefundableException;
import com.example.movieticket.exception.TicketNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Controller  // For serving HTML pages
@RequestMapping("/cancellation")  // Base URL for cancellation
@RequiredArgsConstructor
public class CancellationController {

    private final CancellationService cancellationService;

    // This is the GET method to show the cancellation page
    @GetMapping("/cancel-ticket")
    public String showCancellationPage() {
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
}