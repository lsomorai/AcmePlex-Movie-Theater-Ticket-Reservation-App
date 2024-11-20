package com.example.movieticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieticket.repository.ShowtimeRepository;
import com.example.movieticket.service.ShowtimeService;

@RestController
@RequestMapping(path = "/showtime")
@CrossOrigin
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("movie/{movieId}")
    public List<ShowtimeRepository.ShowtimeBrief> getShowtimesForMovie(@PathVariable Long movieId) {
        return showtimeService.getShowtimesForMovie(movieId);
    }

    @GetMapping("movie/{movieId}/theatre/{theatreId}")
    public List<ShowtimeRepository.ShowtimeBrief> getShowtimesForMovieAndTheater(
            @PathVariable Long movieId, @PathVariable Long theatreId) {
        return showtimeService.getShowtimesForMovieAndTheater(movieId, theatreId);
    }

}
