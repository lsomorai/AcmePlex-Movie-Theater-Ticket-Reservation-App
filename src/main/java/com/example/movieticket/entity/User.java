/*
 * Cory
 */
package com.example.movieticket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NonNull;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String usertype;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String usertype) {
        this.username = username;
        this.password = password;
        this.usertype = usertype;
    }

    public User(int id, String username, String password, String usertype) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.usertype = usertype;
    }

    public User() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getUserType() {
        return usertype;
    }

    public void setUserType(String usertype) {
        this.usertype = usertype;
    }
}