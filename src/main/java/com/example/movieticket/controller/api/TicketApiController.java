package com.example.movieticket.controller.api;

import com.example.movieticket.dto.PaymentRequest;
import com.example.movieticket.dto.response.ApiResponse;
import com.example.movieticket.dto.response.TicketResponse;
import com.example.movieticket.repository.TicketRepository;
import com.example.movieticket.service.CancellationService;
import com.example.movieticket.service.TicketPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Ticket management APIs")
public class TicketApiController {

    private final TicketRepository ticketRepository;
    private final TicketPaymentService ticketPaymentService;
    private final CancellationService cancellationService;

    @GetMapping
    @Operation(summary = "Get all tickets", description = "Retrieve a list of all tickets")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets() {
        List<TicketResponse> tickets = ticketRepository.findAll().stream()
                .map(TicketResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }

    @GetMapping("/{referenceNumber}")
    @Operation(summary = "Get ticket by reference number", description = "Retrieve a specific ticket by its reference number")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketByReferenceNumber(
            @Parameter(description = "Ticket reference number") @PathVariable String referenceNumber) {
        return ticketRepository.findByReferenceNumber(referenceNumber)
                .map(ticket -> ResponseEntity.ok(ApiResponse.success(TicketResponse.fromEntity(ticket))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/purchase")
    @Operation(summary = "Purchase tickets", description = "Process a ticket purchase with payment")
    public ResponseEntity<ApiResponse<Map<String, String>>> purchaseTickets(
            @Valid @RequestBody PaymentRequest paymentRequest,
            @Parameter(description = "User ID") @RequestParam Integer userId) {
        try {
            String referenceNumber = ticketPaymentService.processTicketPayment(paymentRequest, userId);
            return ResponseEntity.ok(ApiResponse.success(
                    "Ticket purchase successful",
                    Map.of("referenceNumber", referenceNumber)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{referenceNumber}/details")
    @Operation(summary = "Get ticket details for cancellation", description = "Retrieve ticket details including refundability status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTicketDetails(
            @Parameter(description = "Ticket reference number") @PathVariable String referenceNumber,
            @Parameter(description = "Current user ID") @RequestParam Integer userId) {
        try {
            Map<String, Object> details = cancellationService.getTicketDetails(referenceNumber, userId);
            return ResponseEntity.ok(ApiResponse.success(details));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{referenceNumber}/cancel")
    @Operation(summary = "Cancel a ticket", description = "Cancel a ticket and process refund if eligible")
    public ResponseEntity<ApiResponse<String>> cancelTicket(
            @Parameter(description = "Ticket reference number") @PathVariable String referenceNumber) {
        try {
            String result = cancellationService.cancelTicket(referenceNumber);
            return ResponseEntity.ok(ApiResponse.success(result, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
