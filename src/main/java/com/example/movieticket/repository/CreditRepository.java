package com.example.movieticket.repository;

import com.example.movieticket.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findByCreditCode(String creditCode);
}
