package com.example.movieticket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movieticket.model.Theatre;
import com.example.movieticket.repository.TheatreRepository;

@Service
public class TheatreService {
    private final TheatreRepository theatreRepository;

    @Autowired
    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

}