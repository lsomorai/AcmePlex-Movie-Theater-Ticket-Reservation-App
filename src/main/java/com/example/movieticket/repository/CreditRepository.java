package com.example.movieticket.repository;

import com.example.movieticket.entity.Credit;
import com.example.movieticket.entity.CreditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findByCreditCodeAndStatusAndExpiryDateGreaterThanEqual(
        String creditCode, 
        CreditStatus status, 
        LocalDate currentDate
    );
}