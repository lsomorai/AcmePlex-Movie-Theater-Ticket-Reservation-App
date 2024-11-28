/*
 * Ticket.java
 * Author: Warisa Khaophong
 * Date: 2024-11-24
 * ENSF 614 2024
*/

package com.example.movieticket.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "paymentid")
    private Long paymentId;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status = TicketStatus.ACTIVE;

    @Column(name = "isRefundable", nullable = false)
    private boolean isRefundable;

    @Column(name = "reference_number", nullable = false, unique = true)
    private String referenceNumber;

    public boolean getIsRefundable() {
        return isRefundable;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setIsRefundable(boolean isRefundable) {
        this.isRefundable = isRefundable;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

}