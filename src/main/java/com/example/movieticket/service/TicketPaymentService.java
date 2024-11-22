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

@Service
@RequiredArgsConstructor
public class TicketPaymentService {
    
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void processTicketPayment(PaymentRequest paymentRequest, Integer userId) {
        // Save payment
        Payment payment = new Payment();
        payment.setCardnumber(paymentRequest.getCardnumber());
        payment.setCardname(paymentRequest.getCardname());
        payment.setExpirydate(paymentRequest.getExpirydate());
        payment.setCvv(paymentRequest.getCvv());
        payment.setAmount(paymentRequest.getAmount());
        paymentRepository.save(payment);

        // Create tickets
        String[] seatIds = paymentRequest.getSelectedSeats().split(",");
        for (String seatId : seatIds) {
            Seat seat = seatRepository.findById(Long.parseLong(seatId)).orElseThrow();
            
            Ticket ticket = new Ticket();
            ticket.setUserId(userId);
            ticket.setMovieId(seat.getShowtime().getMovie().getId());
            ticket.setSeatId(seat.getId());
            ticket.setPurchaseDate(LocalDateTime.now());
            ticket.setStatus(TicketStatus.ACTIVE);
            ticket.setIsRefundable(true);
            ticket.setReferenceNumber(generateReferenceNumber());
            
            ticketRepository.save(ticket);
            
            // Update seat status
            seat.setStatus("BOOKED");
            seatRepository.save(seat);
        }
    }

    private String generateReferenceNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 