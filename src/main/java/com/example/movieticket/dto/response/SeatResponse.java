package com.example.movieticket.dto.response;

import com.example.movieticket.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {

    private Long id;
    private String seatRow;
    private Integer seatNumber;
    private String seatLabel;
    private String seatType;
    private Double price;
    private String status;

    public static SeatResponse fromEntity(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatRow(seat.getSeatRow())
                .seatNumber(seat.getSeatNumber())
                .seatLabel(seat.getSeatRow() + seat.getSeatNumber())
                .seatType(seat.getSeatType())
                .price(seat.getPrice())
                .status(seat.getStatus())
                .build();
    }
}
