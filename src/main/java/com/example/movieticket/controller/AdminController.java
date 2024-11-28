/*
 * AdminController.java
 * Author: Rick Zhang
 * Date: 2024-11-25
 * ENSF 614 2024
*/
package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;

import com.example.movieticket.repository.UserRepository;
import com.example.movieticket.entity.User;
import com.example.movieticket.repository.MovieRepository;
import com.example.movieticket.entity.Movie;
import com.example.movieticket.entity.MovieStatus;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/admin")
    public String adminDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userType = (String) session.getAttribute("userType");
        
        if (!"ADMIN".equals(userType)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access denied: Admin privileges required");
            return "redirect:/dashboard";
        }
        
        String username = (String) session.getAttribute("username");
        model.addAttribute("displayName", username);
        
        return "admin";
    }

    @GetMapping("/admin/notifications")
    public String showNotifications(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userType = (String) session.getAttribute("userType");
        
        if (!"ADMIN".equals(userType)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access denied: Admin privileges required");
            return "redirect:/dashboard";
        }
        
        // Get all regular users with email addresses
        List<User> regularUsers = userRepository.findByUserType("REGULAR");
        
        // Get all coming soon movies
        List<Movie> comingSoonMovies = movieRepository.findByStatus(MovieStatus.COMING_SOON);
        
        model.addAttribute("regularUsers", regularUsers);
        model.addAttribute("comingSoonMovies", comingSoonMovies);
        model.addAttribute("displayName", session.getAttribute("username"));
        
        return "admin-notifications";
    }

    /*
     * Send notifications to users
     */
    @PostMapping("/admin/send-notifications")
    @ResponseBody
    public Map<String, String> sendNotifications(@RequestBody NotificationRequest request) {
        
        
        // Return success response
        return Map.of("status", "success");
    }
}


class NotificationRequest {
    private List<String> emails;
    private List<String> movies;

    // Getters and setters
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }
} 