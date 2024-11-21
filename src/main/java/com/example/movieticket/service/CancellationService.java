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

        // Check if the ticket is eligible for cancellation
        if (!actualTicket.getIsRefundable() || actualTicket.getPurchaseDate().isBefore(LocalDateTime.now().minusHours(72))) {
            logger.error("Ticket is not refundable or not eligible for cancellation (purchased too late).");
            throw new TicketNotRefundableException("Sorry, you are not eligible to cancel this ticket. " +
                    "Cancellation is only allowed up to 72 hours before the show.");
        }

        // Fetch the seat to retrieve the price
        Optional<Seat> seat = seatRepository.findById(actualTicket.getSeatId());
        if (seat.isEmpty()) {
            logger.error("Seat not found for ticket with ID: {}", actualTicket.getSeatId());
            throw new IllegalArgumentException("Seat not found for this ticket");
        }
        Double seatPrice = seat.get().getPrice();

        // Update ticket status
        actualTicket.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(actualTicket);

        // Handle cancellation outcome
        User user = userRepository.findById(actualTicket.getUserId());
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
}
