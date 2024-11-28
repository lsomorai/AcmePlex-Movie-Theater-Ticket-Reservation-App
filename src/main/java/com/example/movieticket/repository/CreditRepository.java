/*
 * CreditRepository.java
 * Author: Warisa Khaophong
 * Date: 2024-11-24
 * ENSF 614 2024
*/

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