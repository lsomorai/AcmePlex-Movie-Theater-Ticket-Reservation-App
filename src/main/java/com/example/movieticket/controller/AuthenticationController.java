package com.example.movieticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.movieticket.entity.User;
import com.example.movieticket.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public String login(User entity, HttpSession session, Model model) {

		if (userRepository.findByUsernamePassword(entity.getUsername(),
				entity.getPassword()).size() > 0) {
			List<User> users = userRepository.findByUsernamePassword(entity.getUsername(),
					entity.getPassword());
			session.setAttribute("username", users.get(0).getUsername());
			return "greeting";
		} else {
			model.addAttribute("errorMessage", "Invalid username or password");
			return "/register";
		}

	}

	@PostMapping("/signup")
	public String signup(User entity, Model model) {
		if (userRepository.findByUsername(entity.getUsername()).size() > 0) {

			model.addAttribute("errorMessage", "Username already exists");
			return "/register";
		} else {
			userRepository.save(entity);
			if (entity.getUserType().equals("RUs")) {

				User user = new User();
				user = userRepository.findByUsername(entity.getUsername()).get(0);
				model.addAttribute("amount", "20.00");
				model.addAttribute("userid", user.getId());
				return "/payment";
			} else {

				model.addAttribute("errorMessage", "sucessful registered a new generl user!");
				return "/register";
			}

		}

	}

	@GetMapping("/logout")
	public String logout(Model model) {
		model.addAttribute("username", null);
		return "/register";
	}

}
