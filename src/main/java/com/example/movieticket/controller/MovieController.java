package com.example.movieticket.controller;

import com.example.movieticket.entity.Movie;
import com.example.movieticket.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies")
    public String listMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "movies";
    }
}



