/*
 * TicketNotFoundException.java
 * Author: Warisa Khaophong
 * Date: 2024-11-24
 * ENSF 614 2024
*/

package com.example.movieticket.exception;


public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}
