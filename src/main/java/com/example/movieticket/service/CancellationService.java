package com.example.movieticket.service;

import com.example.movieticket.entity.*;
import com.example.movieticket.exception.TicketNotFoundException;
import com.example.movieticket.exception.TicketNotRefundableException;
import com.example.movieticket.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancellationService {

    private final TicketRepository ticketRepository;
    private final CreditRepository creditRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private static final Logger logger = LoggerFactory.getLogger(CancellationService.class);

    @Transactional
    public String cancelTicket(String referenceNumber) {
        logger.info("Attempting to cancel ticket with reference number: {}", referenceNumber);

        // Fetch the ticket using the reference number
        Optional<Ticket> ticket = ticketRepository.findByReferenceNumber(referenceNumber);
        if (ticket.isEmpty()) {
            logger.error("Ticket not found for reference number: {}", referenceNumber);
            throw new TicketNotFoundException("Invalid booking reference number");
        }
        Ticket actualTicket = ticket.get();
        logger.info("Ticket found: {}", actualTicket);

        // Get show datetime to check 72-hour rule
        Seat seat = seatRepository.findById(actualTicket.getSeatId())
            .orElseThrow(() -> new IllegalArgumentException("Seat not found for this ticket"));
        
        Showtime showtime = seat.getShowtime();
        LocalDateTime showDateTime = showtime.getDate().atTime(
            showtime.getSession() == 1 ? 10 : 
            showtime.getSession() == 2 ? 14 : 19, 
            0  // minutes
        );

        // Check if ticket is eligible for cancellation
        if (!LocalDateTime.now().plusHours(72).isBefore(showDateTime)) {
            logger.error("Ticket is not eligible for cancellation (less than 72 hours before show time).");
            throw new TicketNotRefundableException("Sorry, you are not eligible to cancel this ticket. " +
                    "Cancellation is only allowed up to 72 hours before the show.");
        }

        Double seatPrice = seat.getPrice();

        // Update ticket status
        actualTicket.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(actualTicket);

        // Handle cancellation outcome
        User user = userRepository.findById(actualTicket.getUserId().longValue()).orElse(null);
        if (user == null) {
            logger.error("User with ID {} not found", actualTicket.getUserId());
        } else {
            logger.info("User found: {}", user);
        }

        if ("RUs".equalsIgnoreCase(user.getUserType())) {
            logger.info("User is a registered user, issuing full refund.");
            return "Your ticket has been canceled successfully. You will receive a full refund.";
        } else {
            // Ordinary user: generate a credit code
            Credit credit = new Credit();
            credit.setCreditCode(CreditService.generateCreditCode());
            credit.setAmount(seatPrice * 0.85); // Deduct 15% fee
            credit.setExpiryDate(LocalDate.now().plusYears(1));
            credit.setStatus(CreditStatus.UNUSED);
            creditRepository.save(credit);

            logger.info("Generated credit code for ordinary user: {}", credit.getCreditCode());
            return "Your ticket has been canceled successfully. Your credit code is: " + credit.getCreditCode();
        }
    }

    public Map<String, Object> getTicketDetails(String referenceNumber, Integer currentUserId) {
        Ticket ticket = ticketRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        // Verify the ticket belongs to the current user
        // For guest users (currentUserId = 1), only show tickets with userId = 1
        if (currentUserId == 1) {
            if (ticket.getUserId() != 1) {
                throw new RuntimeException("You are not authorized to view this ticket");
            }
        } else {
            // For registered users, verify the ticket belongs to them
            if (!ticket.getUserId().equals(currentUserId)) {
                throw new RuntimeException("You are not authorized to view this ticket");
            }
        }

        // Get associated seat and movie details
        Seat seat = seatRepository.findById(ticket.getSeatId())
            .orElseThrow(() -> new RuntimeException("Seat not found"));
        
        Showtime showtime = seat.getShowtime();
        Movie movie = showtime.getMovie();

        // Calculate show datetime
        LocalDateTime showDateTime = showtime.getDate().atTime(
            showtime.getSession() == 1 ? 10 : 
            showtime.getSession() == 2 ? 14 : 19, 
            0  // minutes
        );

        // Check if more than 72 hours before show time
        boolean isRefundable = LocalDateTime.now().plusHours(72).isBefore(showDateTime) 
                              && ticket.getStatus() == TicketStatus.ACTIVE;

        Map<String, Object> details = new HashMap<>();
        details.put("success", true);
        details.put("movieTitle", movie.getTitle());
        details.put("showDate", showtime.getDate().toString());
        details.put("showTime", showtime.getSession() == 1 ? "10:00 AM" : 
                               showtime.getSession() == 2 ? "2:00 PM" : "7:00 PM");
        details.put("seatNumber", seat.getSeatRow() + seat.getSeatNumber());
        details.put("purchaseDate", ticket.getPurchaseDate().toString());
        details.put("isRefundable", isRefundable);
        details.put("status", ticket.getStatus().toString());
        
        return details;
    }
}