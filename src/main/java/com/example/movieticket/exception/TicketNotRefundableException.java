/*
 * TicketNotRefundableException.java
 * Author: Warisa Khaophong
 * Date: 2024-11-24
 * ENSF 614 2024
*/

package com.example.movieticket.exception;

public class TicketNotRefundableException extends RuntimeException {
    public TicketNotRefundableException(String message) {
        super(message);
    }
}
