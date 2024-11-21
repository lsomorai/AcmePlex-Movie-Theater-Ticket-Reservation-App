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

import com.example.movieticket.entity.Payment;
import com.example.movieticket.repository.PaymentRespository;
import com.example.movieticket.entity.User;
import com.example.movieticket.repository.UserRespository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {
    // create a new controller with post method and get payment details from
    // payment.html and insert into payment table,return ture if payment is
    // successful to singup.html
    @Autowired
    private PaymentRespository paymentRespository;

    @PostMapping("/RuPayment")
    public String signup(Payment entity, HttpSession session, Model model) {
        paymentRespository.save(entity);
        model.addAttribute("successMessage", "Payment Successful");
        model.addAttribute("errorMessage", "sucessful registered a new RUs user!");
        return "/register";
    }
}
