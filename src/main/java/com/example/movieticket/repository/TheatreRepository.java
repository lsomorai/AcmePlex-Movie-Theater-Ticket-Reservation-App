package com.example.movieticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieticket.model.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
}