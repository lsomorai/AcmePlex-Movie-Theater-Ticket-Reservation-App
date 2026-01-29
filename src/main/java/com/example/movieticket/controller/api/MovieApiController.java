package com.example.movieticket.controller.api;

import com.example.movieticket.dto.response.ApiResponse;
import com.example.movieticket.dto.response.MovieResponse;
import com.example.movieticket.entity.Movie;
import com.example.movieticket.entity.MovieStatus;
import com.example.movieticket.repository.MovieRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Movie management APIs")
public class MovieApiController {

    private final MovieRepository movieRepository;

    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieve a list of all movies")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getAllMovies() {
        List<MovieResponse> movies = movieRepository.findAll().stream()
                .map(MovieResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(movies));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieve a specific movie by its ID")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(
            @Parameter(description = "Movie ID") @PathVariable Long id) {
        return movieRepository.findById(id)
                .map(movie -> ResponseEntity.ok(ApiResponse.success(MovieResponse.fromEntity(movie))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get movies by status", description = "Retrieve movies filtered by status (NOW_SHOWING or COMING_SOON)")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getMoviesByStatus(
            @Parameter(description = "Movie status") @PathVariable MovieStatus status) {
        List<MovieResponse> movies = movieRepository.findByStatus(status).stream()
                .map(MovieResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(movies));
    }

    @GetMapping("/theatre/{theatreId}")
    @Operation(summary = "Get movies by theatre", description = "Retrieve all movies showing at a specific theatre")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getMoviesByTheatre(
            @Parameter(description = "Theatre ID") @PathVariable Long theatreId) {
        List<MovieResponse> movies = movieRepository.findMoviesByTheatreId(theatreId).stream()
                .map(MovieResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(movies));
    }

    @GetMapping("/search")
    @Operation(summary = "Search movies", description = "Search for movies by title")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> searchMovies(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Movie status filter") @RequestParam(required = false) MovieStatus status) {
        List<Movie> movies;
        if (status != null) {
            movies = movieRepository.findByTitleContainingIgnoreCaseAndStatus(query, status);
        } else {
            movies = movieRepository.findAll().stream()
                    .filter(m -> m.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        List<MovieResponse> response = movies.stream()
                .map(MovieResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
