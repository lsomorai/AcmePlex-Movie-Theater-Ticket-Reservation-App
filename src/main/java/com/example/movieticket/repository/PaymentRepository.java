/*
 * PaymentRepository.java
 * Author: Cory Wu
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import com.example.movieticket.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.cardnumber = ?1 AND p.cardname=?2")
    List<Payment> findByCardNumberCardHolderName(String cardnumber, String cardname);

    @Modifying
    @Query("insert into Payment (cardnumber, cardname, expirydate, cvv, amount) values (?1, ?2, ?3, ?4, ?5)")
    void insertPayment(String cardnumber, String cardname, String expirydate, int cvv, double amount);
}