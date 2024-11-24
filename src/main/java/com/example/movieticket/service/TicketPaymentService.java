package com.example.movieticket.service;

import com.example.movieticket.dto.PaymentRequest;
import com.example.movieticket.entity.Payment;
import com.example.movieticket.entity.Seat;
import com.example.movieticket.entity.Ticket;
import com.example.movieticket.entity.TicketStatus;
import com.example.movieticket.repository.PaymentRepository;
import com.example.movieticket.repository.SeatRepository;
import com.example.movieticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.movieticket.repository.CreditRepository;
import com.example.movieticket.entity.Credit;
import com.example.movieticket.entity.CreditStatus;

@Service
@RequiredArgsConstructor
public class TicketPaymentService {
    
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    @Autowired
    private CreditRepository creditRepository;

    @Transactional
    public String processTicketPayment(PaymentRequest paymentRequest, Integer userId) {
        try {
            // Save payment first to get the payment ID
            Payment payment = new Payment();
            payment.setCardnumber(paymentRequest.getCardnumber());
            payment.setCardname(paymentRequest.getCardname());
            payment.setExpirydate(paymentRequest.getExpirydate());
            payment.setCvv(paymentRequest.getCvv());
            payment.setAmount(paymentRequest.getAmount());
            payment.setUserid(userId);
            payment.setNote("TICKET_PURCHASE");
            
            System.out.println("Saving payment for user: " + userId);
            Payment savedPayment = paymentRepository.save(payment);
            Long paymentId = savedPayment.getId();

            String lastReferenceNumber = null;
            // Create tickets with payment ID
            String[] seatIds = paymentRequest.getSelectedSeats().split(",");
            for (String seatId : seatIds) {
                System.out.println("Processing seat: " + seatId);
                Seat seat = seatRepository.findById(Long.parseLong(seatId))
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));
                
                Ticket ticket = new Ticket();
                ticket.setUserId(userId);
                ticket.setMovieId(seat.getShowtime().getMovie().getId());
                ticket.setSeatId(seat.getId());
                ticket.setPaymentId(paymentId);  // Set the payment ID
                ticket.setPurchaseDate(LocalDateTime.now());
                ticket.setStatus(TicketStatus.ACTIVE);
                ticket.setIsRefundable(true);
                String referenceNumber = generateReferenceNumber();
                ticket.setReferenceNumber(referenceNumber);
                lastReferenceNumber = referenceNumber;
                
                System.out.println("Saving ticket with reference: " + ticket.getReferenceNumber());
                ticketRepository.save(ticket);
                
                // Update seat status
                seat.setStatus("BOOKED");
                seatRepository.save(seat);
            }

            // Only update credit status after successful ticket creation
            if (paymentRequest.getAppliedCreditCode() != null && !paymentRequest.getAppliedCreditCode().isEmpty()) {
                Optional<Credit> credit = creditRepository.findByCreditCodeAndStatusAndExpiryDateGreaterThanEqual(
                    paymentRequest.getAppliedCreditCode(),
                    CreditStatus.UNUSED,
                    LocalDate.now()
                );
                
                if (credit.isPresent()) {
                    Credit usedCredit = credit.get();
                    usedCredit.setStatus(CreditStatus.USED);
                    creditRepository.save(usedCredit);
                    System.out.println("Credit code " + paymentRequest.getAppliedCreditCode() + " marked as USED");
                }
            }

            return lastReferenceNumber;
        } catch (Exception e) {
            System.out.println("Error in processTicketPayment: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    private String generateReferenceNumber() {
        // Find the highest reference number in the database
        String lastRefNumber = ticketRepository.findTopByOrderByReferenceNumberDesc()
                .map(Ticket::getReferenceNumber)
                .orElse("REF00000");  // Default if no tickets exist
        
        // Extract the numeric part and increment
        int currentNumber = Integer.parseInt(lastRefNumber.substring(3));
        int nextNumber = currentNumber + 1;
        
        // Format the new reference number with leading zeros
        return String.format("REF%05d", nextNumber);
    }
} 