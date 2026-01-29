package com.example.movieticket.repository;

import com.example.movieticket.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket testTicket1;
    private Ticket testTicket2;
    private Ticket testTicket3;
    private User testUser;
    private Movie testMovie;
    private Theatre testTheatre;
    private Showtime testShowtime;
    private Seat testSeat;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setUserType("REGULAR");
        entityManager.persist(testUser);

        // Create test theatre
        testTheatre = new Theatre();
        testTheatre.setName("Test Theatre");
        entityManager.persist(testTheatre);

        // Create test movie
        testMovie = new Movie();
        testMovie.setTitle("Test Movie");
        testMovie.setStatus(MovieStatus.NOW_SHOWING);
        entityManager.persist(testMovie);

        // Create test showtime
        testShowtime = new Showtime();
        testShowtime.setMovie(testMovie);
        testShowtime.setTheatre(testTheatre);
        testShowtime.setDate(LocalDate.now().plusDays(5));
        testShowtime.setSession(1);
        entityManager.persist(testShowtime);

        // Create test seat
        testSeat = new Seat();
        testSeat.setTheaterId(1);
        testSeat.setShowtime(testShowtime);
        testSeat.setSeatRow("A");
        testSeat.setSeatNumber(1);
        testSeat.setSeatType("REGULAR");
        testSeat.setPrice(15.0);
        testSeat.setStatus("BOOKED");
        entityManager.persist(testSeat);

        // Create test tickets with sequential reference numbers
        testTicket1 = new Ticket();
        testTicket1.setUserId(testUser.getId());
        testTicket1.setMovieId(testMovie.getId());
        testTicket1.setSeatId(testSeat.getId());
        testTicket1.setPurchaseDate(LocalDateTime.now().minusDays(1));
        testTicket1.setStatus(TicketStatus.ACTIVE);
        testTicket1.setIsRefundable(true);
        testTicket1.setReferenceNumber("REF00001");
        entityManager.persist(testTicket1);

        testTicket2 = new Ticket();
        testTicket2.setUserId(testUser.getId());
        testTicket2.setMovieId(testMovie.getId());
        testTicket2.setSeatId(testSeat.getId());
        testTicket2.setPurchaseDate(LocalDateTime.now());
        testTicket2.setStatus(TicketStatus.ACTIVE);
        testTicket2.setIsRefundable(true);
        testTicket2.setReferenceNumber("REF00002");
        entityManager.persist(testTicket2);

        testTicket3 = new Ticket();
        testTicket3.setUserId(1); // Guest user
        testTicket3.setMovieId(testMovie.getId());
        testTicket3.setSeatId(testSeat.getId());
        testTicket3.setPurchaseDate(LocalDateTime.now());
        testTicket3.setStatus(TicketStatus.CANCELED);
        testTicket3.setIsRefundable(false);
        testTicket3.setReferenceNumber("REF00003");
        entityManager.persist(testTicket3);

        entityManager.flush();
    }

    @Test
    @DisplayName("Should find ticket by ID and user ID")
    void findByIdAndUserId_Success() {
        Optional<Ticket> ticket = ticketRepository.findByIdAndUserId(testTicket1.getId(), testUser.getId());

        assertThat(ticket).isPresent();
        assertThat(ticket.get().getReferenceNumber()).isEqualTo("REF00001");
    }

    @Test
    @DisplayName("Should return empty when ticket ID and user ID don't match")
    void findByIdAndUserId_Mismatch() {
        Optional<Ticket> ticket = ticketRepository.findByIdAndUserId(testTicket1.getId(), 999);

        assertThat(ticket).isEmpty();
    }

    @Test
    @DisplayName("Should return empty for non-existent ticket ID")
    void findByIdAndUserId_NonExistent() {
        Optional<Ticket> ticket = ticketRepository.findByIdAndUserId(999L, testUser.getId());

        assertThat(ticket).isEmpty();
    }

    @Test
    @DisplayName("Should find ticket by reference number")
    void findByReferenceNumber_Success() {
        Optional<Ticket> ticket = ticketRepository.findByReferenceNumber("REF00001");

        assertThat(ticket).isPresent();
        assertThat(ticket.get().getUserId()).isEqualTo(testUser.getId());
        assertThat(ticket.get().getStatus()).isEqualTo(TicketStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should return empty for non-existent reference number")
    void findByReferenceNumber_NonExistent() {
        Optional<Ticket> ticket = ticketRepository.findByReferenceNumber("REF99999");

        assertThat(ticket).isEmpty();
    }

    @Test
    @DisplayName("Should find ticket with highest reference number")
    void findTopByOrderByReferenceNumberDesc() {
        Optional<Ticket> ticket = ticketRepository.findTopByOrderByReferenceNumberDesc();

        assertThat(ticket).isPresent();
        assertThat(ticket.get().getReferenceNumber()).isEqualTo("REF00003");
    }

    @Test
    @DisplayName("Should return empty when no tickets exist")
    void findTopByOrderByReferenceNumberDesc_NoTickets() {
        // Delete all tickets
        ticketRepository.deleteAll();
        entityManager.flush();

        Optional<Ticket> ticket = ticketRepository.findTopByOrderByReferenceNumberDesc();

        assertThat(ticket).isEmpty();
    }

    @Test
    @DisplayName("Should save and retrieve ticket")
    void saveAndRetrieveTicket() {
        Ticket newTicket = new Ticket();
        newTicket.setUserId(testUser.getId());
        newTicket.setMovieId(testMovie.getId());
        newTicket.setSeatId(testSeat.getId());
        newTicket.setPurchaseDate(LocalDateTime.now());
        newTicket.setStatus(TicketStatus.ACTIVE);
        newTicket.setIsRefundable(true);
        newTicket.setReferenceNumber("REF00004");

        Ticket savedTicket = ticketRepository.save(newTicket);

        assertThat(savedTicket.getId()).isNotNull();

        Optional<Ticket> retrievedTicket = ticketRepository.findByReferenceNumber("REF00004");
        assertThat(retrievedTicket).isPresent();
        assertThat(retrievedTicket.get().getIsRefundable()).isTrue();
    }

    @Test
    @DisplayName("Should update ticket status")
    void updateTicketStatus() {
        Optional<Ticket> ticket = ticketRepository.findByReferenceNumber("REF00001");
        assertThat(ticket).isPresent();
        assertThat(ticket.get().getStatus()).isEqualTo(TicketStatus.ACTIVE);

        Ticket ticketToUpdate = ticket.get();
        ticketToUpdate.setStatus(TicketStatus.CANCELED);
        ticketRepository.save(ticketToUpdate);

        entityManager.flush();
        entityManager.clear();

        Optional<Ticket> updatedTicket = ticketRepository.findByReferenceNumber("REF00001");
        assertThat(updatedTicket).isPresent();
        assertThat(updatedTicket.get().getStatus()).isEqualTo(TicketStatus.CANCELED);
    }

    @Test
    @DisplayName("Should find all tickets")
    void findAllTickets() {
        var allTickets = ticketRepository.findAll();

        assertThat(allTickets).hasSize(3);
    }

    @Test
    @DisplayName("Should delete ticket")
    void deleteTicket() {
        Optional<Ticket> ticket = ticketRepository.findByReferenceNumber("REF00001");
        assertThat(ticket).isPresent();

        ticketRepository.delete(ticket.get());

        Optional<Ticket> deletedTicket = ticketRepository.findByReferenceNumber("REF00001");
        assertThat(deletedTicket).isEmpty();
    }

    @Test
    @DisplayName("Should find ticket by ID")
    void findById() {
        Optional<Ticket> ticket = ticketRepository.findById(testTicket1.getId());

        assertThat(ticket).isPresent();
        assertThat(ticket.get().getReferenceNumber()).isEqualTo("REF00001");
    }

    @Test
    @DisplayName("Should handle canceled ticket correctly")
    void findCanceledTicket() {
        Optional<Ticket> ticket = ticketRepository.findByReferenceNumber("REF00003");

        assertThat(ticket).isPresent();
        assertThat(ticket.get().getStatus()).isEqualTo(TicketStatus.CANCELED);
        assertThat(ticket.get().getIsRefundable()).isFalse();
    }

    @Test
    @DisplayName("Should correctly order reference numbers")
    void referenceNumberOrdering() {
        // Add a ticket with higher reference number
        Ticket newTicket = new Ticket();
        newTicket.setUserId(testUser.getId());
        newTicket.setMovieId(testMovie.getId());
        newTicket.setSeatId(testSeat.getId());
        newTicket.setPurchaseDate(LocalDateTime.now());
        newTicket.setStatus(TicketStatus.ACTIVE);
        newTicket.setIsRefundable(true);
        newTicket.setReferenceNumber("REF00010");
        ticketRepository.save(newTicket);

        entityManager.flush();

        Optional<Ticket> topTicket = ticketRepository.findTopByOrderByReferenceNumberDesc();

        assertThat(topTicket).isPresent();
        assertThat(topTicket.get().getReferenceNumber()).isEqualTo("REF00010");
    }
}
