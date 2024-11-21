/*
 * Cory
 */
package com.example.movieticket.controller;

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
import com.example.movieticket.repository.UserRespository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {
	@Autowired
	private UserRespository userRespository;

	@GetMapping("/")
	public String showLoginPage() {
		return "index";
	}

	@GetMapping("/register")
	public String showRegisterPage() {
		return "register";
	}

	@PostMapping("/login")
	public String login(User entity, HttpSession session, Model model) {
		List<User> users = userRespository.findByUsernamePassword(
			entity.getUsername(),
			entity.getPassword()
		);

		if (!users.isEmpty()) {
			session.setAttribute("username", users.get(0).getUsername());
			return "redirect:/theatres";
		} else {
			model.addAttribute("errorMessage", "Invalid username or password");
			return "index";
		}
	}

	@PostMapping("/signup")
	public String signup(User entity, Model model) {
		if (userRespository.findByUsername(entity.getUsername()).size() > 0) {
			model.addAttribute("errorMessage", "Username already exists");
			return "register";
		} else {
			entity.setUserType("REGULAR");
			userRespository.save(entity);
			model.addAttribute("errorMessage", "Registration successful! Please login.");
			return "index";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
