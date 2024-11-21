package com.example.movieticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.movieticket.entity.Payment;
import com.example.movieticket.repository.PaymentRespository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

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
