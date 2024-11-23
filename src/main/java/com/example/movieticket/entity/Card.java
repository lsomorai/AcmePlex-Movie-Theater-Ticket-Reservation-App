package com.example.movieticket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cards")
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cardnumber", nullable = false)
    private String cardNumber;

    @Column(name = "expirydate", nullable = false)
    private String expiryDate;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    // Default constructor
    public Card() {
    }

    // Constructor with fields
    public Card(String cardNumber, String expiryDate, User user) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
} 