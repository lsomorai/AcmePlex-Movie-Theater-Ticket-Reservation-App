package com.example.movieticket.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String cardnumber;
    private String cardname;
    private String expirydate;
    private String cvv;
    private double amount;
    private String selectedSeats;
    private Long showtimeId;
    private String appliedCreditCode;
    private Double appliedCreditAmount;
} 