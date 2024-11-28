/*
 * NameRepository.java
 * Author: Cory Wu
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.movieticket.entity.Name;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {
    
    // Find name by username
    @Query("SELECT n FROM Name n WHERE n.user.username = ?1")
    Name findByUsername(String username);
    
    // Find name by user id
    @Query("SELECT n FROM Name n WHERE n.user.id = ?1")
    Name findByUserId(int userId);
    
    // Check if name exists for a user
    @Query("SELECT COUNT(n) > 0 FROM Name n WHERE n.user.username = ?1")
    boolean existsByUsername(String username);
    
    // Find by first and last name
    @Query("SELECT n FROM Name n WHERE n.first = ?1 AND n.last = ?2")
    Name findByFirstAndLastName(String firstName, String lastName);
} 