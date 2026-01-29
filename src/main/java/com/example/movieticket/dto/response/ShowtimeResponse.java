package com.example.movieticket.dto.response;

import com.example.movieticket.entity.Showtime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeResponse {

    private Long id;
    private LocalDate date;
    private int session;
    private String sessionTime;
    private MovieResponse movie;
    private TheatreResponse theatre;

    public static ShowtimeResponse fromEntity(Showtime showtime) {
        String sessionTime = switch (showtime.getSession()) {
            case 1 -> "10:00 AM";
            case 2 -> "2:00 PM";
            case 3 -> "7:00 PM";
            default -> "Unknown";
        };

        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .date(showtime.getDate())
                .session(showtime.getSession())
                .sessionTime(sessionTime)
                .movie(MovieResponse.fromEntity(showtime.getMovie()))
                .theatre(TheatreResponse.fromEntity(showtime.getTheatre()))
                .build();
    }
}
