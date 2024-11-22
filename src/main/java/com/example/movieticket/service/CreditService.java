package com.example.movieticket.service;

import com.example.movieticket.entity.Credit;

import java.security.SecureRandom;

public class CreditService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 5;  // You can change this length as needed
    private static final SecureRandom random = new SecureRandom();

    public static String generateCreditCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }

    public void createCredit() {
        Credit credit = new Credit();
        credit.setCreditCode(generateCreditCode());  // Set the shorter random code
        // Other credit fields
    }
}
