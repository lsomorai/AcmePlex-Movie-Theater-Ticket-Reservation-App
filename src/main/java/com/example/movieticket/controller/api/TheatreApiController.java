package com.example.movieticket.controller.api;

import com.example.movieticket.dto.response.ApiResponse;
import com.example.movieticket.dto.response.TheatreResponse;
import com.example.movieticket.repository.TheatreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
@Tag(name = "Theatres", description = "Theatre management APIs")
public class TheatreApiController {

    private final TheatreRepository theatreRepository;

    @GetMapping
    @Operation(summary = "Get all theatres", description = "Retrieve a list of all theatres")
    public ResponseEntity<ApiResponse<List<TheatreResponse>>> getAllTheatres() {
        List<TheatreResponse> theatres = theatreRepository.findAll().stream()
                .map(TheatreResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(theatres));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get theatre by ID", description = "Retrieve a specific theatre by its ID")
    public ResponseEntity<ApiResponse<TheatreResponse>> getTheatreById(
            @Parameter(description = "Theatre ID") @PathVariable Long id) {
        return theatreRepository.findById(id)
                .map(theatre -> ResponseEntity.ok(ApiResponse.success(TheatreResponse.fromEntity(theatre))))
                .orElse(ResponseEntity.notFound().build());
    }
}
