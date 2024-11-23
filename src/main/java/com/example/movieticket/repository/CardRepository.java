package com.example.movieticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.movieticket.entity.Card;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    
    // Find all cards by username
    @Query("SELECT c FROM Card c WHERE c.user.username = ?1")
    List<Card> findByUsername(String username);
    
    // Find card by card number
    Card findByCardNumber(String cardNumber);
    
    // Check if card exists for a user
    boolean existsByCardNumberAndUserUsername(String cardNumber, String username);
} 