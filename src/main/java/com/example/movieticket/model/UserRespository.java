package com.example.movieticket.model;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRespository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.password=?2")

    List<User> findByUsernamePassword(String username, String password);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    List<User> findByUsername(String username);

}