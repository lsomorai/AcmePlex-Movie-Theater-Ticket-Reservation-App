/*
 * TheatreRepository.java
 * Author: Lucien Somorai
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movieticket.entity.Theatre;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    // Basic CRUD operations are automatically provided
} 