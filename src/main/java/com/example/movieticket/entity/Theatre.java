/*
 * Lucien
 */
package com.example.movieticket.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import java.time.LocalDateTime;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "theaters")
@Data
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

}
