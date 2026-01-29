package com.example.movieticket.controller.api;

import com.example.movieticket.dto.response.ApiResponse;
import com.example.movieticket.dto.response.SeatResponse;
import com.example.movieticket.dto.response.ShowtimeResponse;
import com.example.movieticket.repository.SeatRepository;
import com.example.movieticket.repository.ShowtimeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
@Tag(name = "Showtimes", description = "Showtime management APIs")
public class ShowtimeApiController {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    @GetMapping
    @Operation(summary = "Get all showtimes", description = "Retrieve a list of all showtimes")
    public ResponseEntity<ApiResponse<List<ShowtimeResponse>>> getAllShowtimes() {
        List<ShowtimeResponse> showtimes = showtimeRepository.findAll().stream()
                .map(ShowtimeResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(showtimes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get showtime by ID", description = "Retrieve a specific showtime by its ID")
    public ResponseEntity<ApiResponse<ShowtimeResponse>> getShowtimeById(
            @Parameter(description = "Showtime ID") @PathVariable Long id) {
        return showtimeRepository.findById(id)
                .map(showtime -> ResponseEntity.ok(ApiResponse.success(ShowtimeResponse.fromEntity(showtime))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/theatre/{theatreId}/movie/{movieId}")
    @Operation(summary = "Get showtimes by theatre and movie", description = "Retrieve showtimes for a specific movie at a specific theatre")
    public ResponseEntity<ApiResponse<List<ShowtimeResponse>>> getShowtimesByTheatreAndMovie(
            @Parameter(description = "Theatre ID") @PathVariable Long theatreId,
            @Parameter(description = "Movie ID") @PathVariable Long movieId) {
        List<ShowtimeResponse> showtimes = showtimeRepository.findByTheatreIdAndMovieId(theatreId, movieId).stream()
                .map(ShowtimeResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(showtimes));
    }

    @GetMapping("/{id}/seats")
    @Operation(summary = "Get seats for showtime", description = "Retrieve all seats and their availability for a specific showtime")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getSeatsForShowtime(
            @Parameter(description = "Showtime ID") @PathVariable Long id) {
        List<SeatResponse> seats = seatRepository.findByShowtimeId(id).stream()
                .map(SeatResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(seats));
    }
}
