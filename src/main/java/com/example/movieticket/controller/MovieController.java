package com.example.movieticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieticket.model.Movie;
import com.example.movieticket.service.MovieService;

@RestController
@RequestMapping(path = "/movie")
@CrossOrigin
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(path = "/all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }
}