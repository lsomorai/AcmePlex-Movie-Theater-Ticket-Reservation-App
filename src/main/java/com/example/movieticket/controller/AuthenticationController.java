/*
 * AuthenticationController.java
 * Author: Cory Wu
 * Date: 2024-11-22
 * ENSF 614 2024
*/
package com.example.movieticket.controller;

import java.util.Map;
import java.util.Collections;

import java.util.List;

import com.example.movieticket.entity.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@SessionAttributes({"pendingUser", "pendingName"})
public class AuthenticationController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String showLoginPage(@RequestParam(required = false) String returnUrl, Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("returnUrl", returnUrl);
		return "index";
	}

	@GetMapping("/register")
	public String showRegisterPage() {
		return "register";
	}

	@PostMapping("/login")
	public String login(User entity, HttpSession session, Model model,
	                   @RequestParam(required = false) String returnUrl) {
		List<User> users = userRepository.findByUsername(entity.getUsername());

		if (!users.isEmpty()) {
			User user = users.get(0);
			// Verify password using BCrypt
			if (passwordEncoder.matches(entity.getPassword(), user.getPassword())) {
				session.setAttribute("username", user.getUsername());
				session.setAttribute("userId", user.getId());
				session.setAttribute("userType", user.getUserType());

				if (returnUrl != null && !returnUrl.isEmpty()) {
					return "redirect:" + returnUrl;
				}
				return "redirect:/dashboard";
			}
		}

		model.addAttribute("user", new User());
		model.addAttribute("errorMessage", "Invalid username or password");
		model.addAttribute("isError", true);
		model.addAttribute("returnUrl", returnUrl);
		return "index";
	}

	@PostMapping("/signup")
	public String signup(@RequestParam String firstName,
                        @RequestParam String lastName,
                        User entity,
                        Model model) {
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

		// Store complete user object in model with hashed password
		User pendingUser = new User();
		pendingUser.setUsername(entity.getUsername());
		pendingUser.setPassword(passwordEncoder.encode(entity.getPassword()));
		pendingUser.setUserType("REGULAR");

		// Create pending name object
		Name pendingName = new Name();
		pendingName.setFirst(firstName);
		pendingName.setLast(lastName);

		model.addAttribute("pendingUser", pendingUser);
		model.addAttribute("pendingName", pendingName);
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

	@ModelAttribute("pendingName")
	public Name pendingName() {
		return new Name();
	}

	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session, @RequestParam(required = false) Boolean guest) {
		String username = (String) session.getAttribute("username");
		
		// Handle guest access
		if (guest != null && guest) {
			model.addAttribute("displayName", "Ordinary User");
			session.setAttribute("username", "Ordinary User");
			session.setAttribute("userId", 1);
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
