package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.example.movieticket.entity.Movie;
import com.example.movieticket.entity.Theatre;
import com.example.movieticket.entity.MovieStatus;
import com.example.movieticket.repository.MovieRepository;
import com.example.movieticket.repository.ShowtimeRepository;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @GetMapping("/dashboard/search")
    public String showDashboard(@RequestParam(required = false) String query, 
                              Model model, 
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("displayName", username != null ? username : "Ordinary User");
        
        List<Movie> movies;
        if (query != null && !query.trim().isEmpty()) {
            // Search for movies if query exists
            movies = movieRepository.findByTitleContainingIgnoreCaseAndStatus(
                query.trim(), MovieStatus.NOW_SHOWING);
            if (movies.isEmpty()) {
                movies = movieRepository.findByStatus(MovieStatus.NOW_SHOWING);
            }
        } else {
            // Show all movies if no query
            movies = movieRepository.findByStatus(MovieStatus.NOW_SHOWING);
        }
        
        // Get theatres for each movie
        Map<Long, List<Theatre>> theatresByMovie = getTheatresByMovies(movies);
        
        model.addAttribute("searchQuery", query);
        model.addAttribute("movies", movies);
        model.addAttribute("theatresByMovie", theatresByMovie);
        
        return "dashboard";
    }
    
    private Map<Long, List<Theatre>> getTheatresByMovies(List<Movie> movies) {
        Map<Long, List<Theatre>> theatresByMovie = new HashMap<>();
        for (Movie movie : movies) {
            List<Theatre> theatres = showtimeRepository.findTheatresByMovieId(movie.getId());
            theatresByMovie.put(movie.getId(), theatres);
        }
        return theatresByMovie;
    }
} 