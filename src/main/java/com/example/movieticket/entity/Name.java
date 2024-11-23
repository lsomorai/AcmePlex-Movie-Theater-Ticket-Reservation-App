package com.example.movieticket.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "names")
@Data
public class Name {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String first;

    @Column(nullable = false)
    private String last;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    // Default constructor
    public Name() {
    }

    // Constructor with fields
    public Name(String first, String last, User user) {
        this.first = first;
        this.last = last;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
} 