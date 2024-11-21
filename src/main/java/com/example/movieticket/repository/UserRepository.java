package com.example.movieticket.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.movieticket.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.password=?2")

    List<User> findByUsernamePassword(String username, String password);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    List<User> findByUsername(String username);

    User findById(int userId);
}