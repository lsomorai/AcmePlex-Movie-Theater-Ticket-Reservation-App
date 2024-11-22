/*
 * Cory
 */
package com.example.movieticket.controller;

import java.util.Map;
import java.util.Collections;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.movieticket.entity.User;
import com.example.movieticket.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SessionAttributes("pendingUser")
public class AuthenticationController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String showLoginPage(Model model) {
		model.addAttribute("user", new User());
		return "index";
	}

	@GetMapping("/register")
	public String showRegisterPage() {
		return "register";
	}

	@PostMapping("/login")
	public String login(User entity, HttpSession session, Model model) {
		List<User> users = userRepository.findByUsernamePassword(
			entity.getUsername(),
			entity.getPassword()
		);

		if (!users.isEmpty()) {
			session.setAttribute("username", users.get(0).getUsername());
			return "redirect:/dashboard";
		} else {
			model.addAttribute("user", new User());
			model.addAttribute("errorMessage", "Invalid username or password");
			model.addAttribute("isError", true);
			return "index";
		}
	}

	@PostMapping("/signup")
	public String signup(User entity, Model model) {
		// Validate username length
		if (entity.getUsername() == null || entity.getUsername().length() < 5) {
			model.addAttribute("errorMessage", "Username must be at least 5 characters long");
			model.addAttribute("isError", true);
			return "register";
		}
		
		// Validate password length
		if (entity.getPassword() == null || entity.getPassword().length() < 5) {
			model.addAttribute("errorMessage", "Password must be at least 5 characters long");
			model.addAttribute("isError", true);
			return "register";
		}

		// Check if username already exists
		if (userRepository.findByUsername(entity.getUsername()).size() > 0) {
			model.addAttribute("errorMessage", "Username already exists");
			model.addAttribute("isError", true);
			return "register";
		}
		
		// Store complete user object in model
		User pendingUser = new User();
		pendingUser.setUsername(entity.getUsername());
		pendingUser.setPassword(entity.getPassword());
		pendingUser.setUserType("REGULAR");
		
		model.addAttribute("pendingUser", pendingUser);
		model.addAttribute("username", entity.getUsername());
		model.addAttribute("amount", "20.00");
		
		return "payment";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/verify-username")
	public @ResponseBody Map<String, Boolean> verifyUsername(@RequestParam String username) {
		// Validate username length
		if (username == null || username.length() < 5) {
			return Collections.singletonMap("available", false);
		}

		// Check if username exists
		boolean isAvailable = userRepository.findByUsername(username).isEmpty();
		return Collections.singletonMap("available", isAvailable);
	}

	@ModelAttribute("pendingUser")
	public User pendingUser() {
		return new User();
	}

	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session, @RequestParam(required = false) Boolean guest) {
		String username = (String) session.getAttribute("username");
		
		// Handle guest access
		if (guest != null && guest) {
			model.addAttribute("displayName", "Ordinary User");
			session.setAttribute("username", "Ordinary User");
			return "dashboard";
		}
		
		// Handle regular user access
		if (username == null) {
			return "redirect:/";  // Redirect to login if not logged in and not guest
		}
		
		model.addAttribute("displayName", username);
		return "dashboard";
	}
}
