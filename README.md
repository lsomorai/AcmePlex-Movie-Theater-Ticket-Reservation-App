# AcmePlex Movie Theater Ticket Reservation App

**ENSF 614 - Fall 2024 Term Project**  
**Department of Electrical and Computer Engineering**  
**Principles of Software Design**

## Project Overview

This project involves designing and implementing a **Movie Theater Ticket Reservation Application** for a fictional company, AcmePlex. The app allows customers to search for movies, select showtimes, choose seats, and make payments for tickets. The system supports two types of users:

1. **Ordinary Users**: Can search and book tickets, with a 15% cancellation fee (up to 72 hours before the show).
2. **Registered Users**: Pay a $20 annual membership fee and enjoy perks like no cancellation fees, early access to tickets, and exclusive news.

This project focuses on applying software design principles, emphasizing modularity, scalability, and maintainability.

---

## Features

### **User Management**
- **Ordinary Users**: Basic ticket booking and cancellation features.
- **Registered Users**: Additional benefits like no cancellation fees and early access to 10% of seats.

### **Movie Search and Selection**
- Browse movies by theater or showtime.

### **Graphical Seat Selection**
- A visual interface for selecting available seats.

### **Ticket Booking and Payment**
- Secure payment process supporting credit card transactions.
- Receipts and tickets sent via email.

### **Cancellation and Refunds**
- Tickets can be canceled up to 72 hours before the show for a credit.
- Registered users are exempt from the 15% cancellation fee.

---

## System Design

The system is implemented using a multi-layered architecture:

### **1. Presentation Layer**
- Handles user interface and interaction, including seat selection and confirmation dialogs.

### **2. Domain Layer**
- Core business logic, such as user roles, payment processing, ticketing, and cancellations.

### **3. Data Layer**
- Manages data storage and retrieval using a MySQL database.

---

## Key Design Diagrams

- **Activity Diagrams**: Outline workflows for browsing, booking, and payment processes.
- **Use Case Diagrams**: Show interactions between users and system functionalities.
- **Class Diagrams**: Define system classes, attributes, behaviors, and relationships.
- **State Transition Diagrams**: Represent states for objects like tickets and payments.
- **Package Diagram**: Demonstrates how layers interact with each other.
- **Deployment Diagram**: Illustrates client-server-database architecture.
