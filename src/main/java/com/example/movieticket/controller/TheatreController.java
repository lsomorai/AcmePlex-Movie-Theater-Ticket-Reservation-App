package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.movieticket.entity.Theatre;
import com.example.movieticket.repository.TheatreRepository;
import java.util.List;

@Controller
public class TheatreController {

    @Autowired
    private TheatreRepository theatreRepository;

    @GetMapping("/theatres")
    public String showTheatres(Model model) {
        List<Theatre> theatres = theatreRepository.findAll();
        model.addAttribute("theatres", theatres);
        return "theatres";
    }
} 