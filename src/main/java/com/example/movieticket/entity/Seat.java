/*
 * Seat.java
 * Author: Lucien Somorai
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.entity;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theater_id", nullable = false)
    private Integer theaterId;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "seat_row", nullable = false)
    private String seatRow;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "seat_type")
    private String seatType;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "status", nullable = false)
    private String status = "AVAILABLE";
} 