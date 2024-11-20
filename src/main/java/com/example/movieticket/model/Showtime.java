package com.example.movieticket.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.lang.NonNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @ManyToOne
    private Movie movie;

    @NonNull
    @ManyToOne
    private Theatre theatre;

    @NonNull
    private LocalDate date;

    @NonNull
    private LocalTime time;

    public Showtime() {
    }

    public Showtime(Movie movie, Theatre theatre, LocalDate date, LocalTime time) {
        this.movie = movie;
        this.theatre = theatre;
        this.date = date;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}