package com.example.movieticket.controller;

import com.example.movieticket.entity.Movie;
import com.example.movieticket.repository.MovieRepository;
import com.example.movieticket.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;

@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private TheatreRepository theatreRepository;

    @GetMapping("/theatres/{theatreId}/movies")
    public String showMovies(@PathVariable Long theatreId, Model model, HttpSession session) {
        model.addAttribute("movies", movieRepository.findMoviesByTheatreId(theatreId));
        model.addAttribute("theatre", theatreRepository.findById(theatreId).orElse(null));
        
        String username = (String) session.getAttribute("username");
        model.addAttribute("displayName", username != null ? username : "Ordinary User");
        
        return "movies";
    }

    @GetMapping("/movies")
    public String listAllMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "movies";
    }
} 