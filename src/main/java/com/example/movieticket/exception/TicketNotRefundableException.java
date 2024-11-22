package com.example.movieticket.exception;

public class TicketNotRefundableException extends RuntimeException {
    public TicketNotRefundableException(String message) {
        super(message);
    }
}
