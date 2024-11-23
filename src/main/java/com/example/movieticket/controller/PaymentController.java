package com.example.movieticket.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.movieticket.entity.Payment;
import com.example.movieticket.repository.PaymentRepository;
import com.example.movieticket.entity.User;
import com.example.movieticket.repository.UserRepository;
import com.example.movieticket.entity.Name;
import com.example.movieticket.repository.NameRepository;
import com.example.movieticket.entity.Card;
import com.example.movieticket.repository.CardRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SessionAttributes({"pendingUser", "pendingName"})
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NameRepository nameRepository;
    
    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/RuPayment")
    @ResponseBody
    public String processPayment(@RequestBody Payment payment, 
                               @ModelAttribute("pendingUser") User pendingUser,
                               @ModelAttribute("pendingName") Name pendingName) {
        try {
            // Create and save user
            User user = new User();
            user.setUsername(pendingUser.getUsername());
            user.setPassword(pendingUser.getPassword());
            user.setUserType("REGULAR");
            user.setExpirationDate(LocalDateTime.now().plusYears(1));
            
            // Generate and set email
            String email = pendingUser.getUsername() + "@ensf614.com";
            user.setEmail(email);
            
            User savedUser = userRepository.save(user);
            
            // Create and save name
            Name name = new Name();
            name.setFirst(pendingName.getFirst());
            name.setLast(pendingName.getLast());
            name.setUser(savedUser);
            nameRepository.save(name);
            
            // Create and save card
            Card card = new Card();
            card.setCardNumber(payment.getCardnumber());
            card.setExpiryDate(payment.getExpirydate());
            card.setUser(savedUser);
            cardRepository.save(card);
            
            // Save payment
            payment.setUserid(savedUser.getId());
            payment.setNote("REGISTRY");
            paymentRepository.save(payment);
            
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
