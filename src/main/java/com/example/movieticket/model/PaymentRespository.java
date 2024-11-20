package com.example.movieticket.model;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface PaymentRespository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.cardnumber = ?1 AND p.cardname=?2")
    List<Payment> findByCardNumberCardHolderName(String cardnumber, String cardname);

    @Modifying
    @Query("insert into Payment (cardnumber, cardname, expirydate, cvv, amount) values (?1, ?2, ?3, ?4, ?5)")
    void insertPayment(String cardnumber, String cardname, String expirydate, int cvv, double amount);
}