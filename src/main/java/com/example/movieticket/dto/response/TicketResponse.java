package com.example.movieticket.dto.response;

import com.example.movieticket.entity.Ticket;
import com.example.movieticket.entity.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

    private Long id;
    private String referenceNumber;
    private Long movieId;
    private Long seatId;
    private LocalDateTime purchaseDate;
    private TicketStatus status;
    private boolean refundable;

    public static TicketResponse fromEntity(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .referenceNumber(ticket.getReferenceNumber())
                .movieId(ticket.getMovieId())
                .seatId(ticket.getSeatId())
                .purchaseDate(ticket.getPurchaseDate())
                .status(ticket.getStatus())
                .refundable(ticket.getIsRefundable())
                .build();
    }
}
