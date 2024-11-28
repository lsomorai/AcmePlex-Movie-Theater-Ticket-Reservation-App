/*
 * MovieController.java
 * Author: Lucien Somorai
 * Date: 2024-11-2
 * ENSF 614 2024
*/

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
        session.setAttribute("currentTheatreId", theatreId);
        
        model.addAttribute("movies", movieRepository.findMoviesByTheatreId(theatreId));
        model.addAttribute("theatre", theatreRepository.findById(theatreId).orElse(null));
        
        String username = (String) session.getAttribute("username");
        boolean isRegisteredUser = username != null && !username.equals("Ordinary User");
        
        model.addAttribute("displayName", username != null ? username : "Ordinary User");
        model.addAttribute("isRegisteredUser", isRegisteredUser);
        
        return "movies";
    }

    @GetMapping("/movies")
    public String listAllMovies(Model model, HttpSession session) {
        session.removeAttribute("currentTheatreId");
        
        String username = (String) session.getAttribute("username");
        boolean isRegisteredUser = username != null && !username.equals("Ordinary User");
        
        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("isRegisteredUser", isRegisteredUser);
        model.addAttribute("displayName", username != null ? username : "Ordinary User");
        model.addAttribute("theatre", null);
        return "movies";
    }
} 