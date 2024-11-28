/*
 * TheatreController.java
 * Author: Lucien Somorai
 * Date: 2024-11-22
 * ENSF 614 2024
*/

package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.movieticket.entity.Theatre;
import com.example.movieticket.repository.TheatreRepository;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
public class TheatreController {

    @Autowired
    private TheatreRepository theatreRepository;

    @GetMapping("/theatres")
    public String showTheatres(Model model, HttpSession session) {
        List<Theatre> theatres = theatreRepository.findAll();
        model.addAttribute("theatres", theatres);
        
        // Get username from session and set consistent display name
        String username = (String) session.getAttribute("username");
        model.addAttribute("displayName", username != null ? username : "Ordinary User");
        
        // Add isRegisteredUser attribute for consistency
        boolean isRegisteredUser = username != null && !username.equals("Ordinary User");
        model.addAttribute("isRegisteredUser", isRegisteredUser);
        
        return "theatres";
    }
} 