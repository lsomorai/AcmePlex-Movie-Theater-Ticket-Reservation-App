package com.example.movieticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieticket.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
