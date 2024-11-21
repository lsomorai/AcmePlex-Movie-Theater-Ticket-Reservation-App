/*
 * Lucien
 */
package com.example.movieticket.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "showtimes")
@Data
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theatre theatre;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "session", nullable = false)
    private int session;
}
