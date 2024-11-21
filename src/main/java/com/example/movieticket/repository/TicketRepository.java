package com.example.movieticket.repository;

import com.example.movieticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByIdAndUserId(Long id, Integer userId); // Use optional being to handle not found tickets
    Optional<Ticket> findByReferenceNumber(String referenceNumber); // Use optional to handle not found tickets

}
