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

---

## Deployment Instructions  

### Follow these steps to deploy the project on your local device:  

### **Step 1**: Download the Source Code  
- Clone the Git repository.  

### **Step 2**: Install Necessary Dependencies  
This is a **Spring Boot Gradle project**.  
- Ensure the following prerequisites are installed:  
  - **Java JDK 17** or higher  
  - **Gradle 8.x** (or use the included Gradle wrapper)  
  - **MySQL 8.x**  
  - **IDE** (recommended: IntelliJ IDEA or Eclipse)  

### **Step 3**: Configure the MySQL Database  
- By default, the application uses `<username=root>` and no password.  
- Update the `<application.properties>` file to match your database credentials if necessary.  
- The application will create an empty MySQL schema named `<acmeplex>`. If a schema with the same name exists, rename or drop it manually.  

### **Step 4**: Build and Run the Application  
- Navigate to the project directory and run the following command:  
  ```bash
  ./gradlew clean build bootRun

### Step 5: Verify Application Deployment  
- Open your browser and navigate to [http://localhost:8080](http://localhost:8080) to confirm the app is running.  

### Step 6: Initialize the Database  
- Open **MySQL Workbench** and ensure the schema `<acmeplex>` is created in **Local Instance 3306**.
- Run the script `Group9_SQL.sql` located in the `<sqlscript>` folder to populate the database.   

### Step 7: Test the Application  
- Perform the following actions to verify the functionality of the application:  
  - Create an account.  
  - Purchase a ticket.  
  - Cancel a ticket.  
  - Ensure the expected outcomes occur, such as confirmation messages or email notifications.  

### Step 8: Clean Up After Testing  
- Drop the `<acmeplex>` schema once you finish testing to clean up your database environment.  
