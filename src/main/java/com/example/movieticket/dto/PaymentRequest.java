/*
 * PaymentRequest.java
 * Author: Rick Zhang
 * Date: 2024-11-25
 * ENSF 614 2024
*/

package com.example.movieticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^\\d{13,19}$", message = "Card number must be between 13 and 19 digits")
    private String cardnumber;

    @NotBlank(message = "Cardholder name is required")
    @Size(min = 2, max = 100, message = "Cardholder name must be between 2 and 100 characters")
    private String cardname;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Expiry date must be in MM/YY format")
    private String expirydate;

    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^\\d{3,4}$", message = "CVV must be 3 or 4 digits")
    private String cvv;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "At least one seat must be selected")
    private String selectedSeats;

    @NotNull(message = "Showtime is required")
    private Long showtimeId;

    private String appliedCreditCode;
    private Double appliedCreditAmount;
} 