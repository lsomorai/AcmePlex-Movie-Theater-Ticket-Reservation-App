package com.example.movieticket.exception;


public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}

