package com.example.movieticket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movieticket.model.Showtime;
import com.example.movieticket.repository.ShowtimeRepository;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    public Showtime getShowtimeById(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
            .orElseThrow(() -> new IllegalStateException("Showtime not found"));
    }

    public List<ShowtimeRepository.ShowtimeBrief> getShowtimesForMovie(Long movieId) {
        return showtimeRepository.findShowtimeByMovieId(movieId);
    }

    public List<ShowtimeRepository.ShowtimeBrief> getShowtimesForMovieAndTheater(Long movieId, Long theatreId) {
        return showtimeRepository.getShowtimesForMovieAndTheater(movieId, theatreId);
    }

}