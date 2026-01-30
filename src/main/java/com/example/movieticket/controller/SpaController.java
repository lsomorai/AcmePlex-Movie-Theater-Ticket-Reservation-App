package com.example.movieticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    /**
     * Forward all non-API, non-static routes to index.html for React Router to handle.
     * This enables client-side routing in the SPA.
     */
    @GetMapping(value = {
            "/",
            "/login",
            "/register",
            "/dashboard",
            "/movies",
            "/movies/**",
            "/theatres",
            "/theatres/**",
            "/showtimes",
            "/showtimes/**",
            "/seats",
            "/seats/**",
            "/ticket-confirmation",
            "/ticket-payment",
            "/booking-success",
            "/cancellation",
            "/cancellation/**",
            "/admin",
            "/admin/**",
            "/payment",
            "/profile"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
