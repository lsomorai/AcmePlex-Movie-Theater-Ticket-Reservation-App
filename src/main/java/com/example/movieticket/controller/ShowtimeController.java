package com.example.movieticket.controller;

import com.example.movieticket.repository.ShowtimeRepository;
import com.example.movieticket.repository.MovieRepository;
import com.example.movieticket.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShowtimeController {

    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private TheatreRepository theatreRepository;

    @GetMapping("/theatres/{theatreId}/movies/{movieId}/showtimes")
    public String showShowtimes(@PathVariable Long theatreId, 
                              @PathVariable Long movieId, 
                              Model model) {
        model.addAttribute("showtimes", showtimeRepository.findByTheatreIdAndMovieId(theatreId, movieId));
        model.addAttribute("theatre", theatreRepository.findById(theatreId).orElse(null));
        model.addAttribute("movie", movieRepository.findById(movieId).orElse(null));
        return "showtimes";
    }
} 