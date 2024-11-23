/*
 * Lucien
 */
package com.example.movieticket.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.NonNull;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MovieStatus status = MovieStatus.NOW_SHOWING;  // Default value

    // Even though @Data provides getters/setters, adding explicit ones for clarity
    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }
}
