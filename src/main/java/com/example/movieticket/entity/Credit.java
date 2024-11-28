/*
 * Credit.java
 * Author: Warisa Khaophong
 * Date: 2024-11-24
 * ENSF 614 2024
*/

package com.example.movieticket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "credits")
@Data
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "credit_code", nullable = false, unique = true)
    private String creditCode; // Unique, sharable code.

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CreditStatus status = CreditStatus.UNUSED;
}