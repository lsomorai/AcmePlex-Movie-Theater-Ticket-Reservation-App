package com.example.movieticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieticket.model.Theatre;
import com.example.movieticket.service.TheatreService;

@RestController
@RequestMapping(path = "/theatre")
@CrossOrigin
public class TheatreController {
    private final TheatreService theatreService;

    @Autowired
    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @GetMapping(path = "/all")
    public List<Theatre> getAllTheatres() {
        return theatreService.getAllTheatres();
    }
}
