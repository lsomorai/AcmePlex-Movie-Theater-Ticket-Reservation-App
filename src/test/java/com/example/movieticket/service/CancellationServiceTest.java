package com.example.movieticket.service;

import com.example.movieticket.entity.*;
import com.example.movieticket.exception.TicketNotFoundException;
import com.example.movieticket.exception.TicketNotRefundableException;
import com.example.movieticket.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancellationServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CancellationService cancellationService;

    private Ticket testTicket;
    private Seat testSeat;
    private Showtime testShowtime;
    private Movie testMovie;
    private Payment testPayment;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup test movie
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setStatus(MovieStatus.NOW_SHOWING);

        // Setup test showtime - 5 days in the future (well beyond 72 hours)
        testShowtime = new Showtime();
        testShowtime.setId(1L);
        testShowtime.setMovie(testMovie);
        testShowtime.setDate(LocalDate.now().plusDays(5));
        testShowtime.setSession(1); // 10 AM

        // Setup test seat
        testSeat = new Seat();
        testSeat.setId(1L);
        testSeat.setTheaterId(1);
        testSeat.setShowtime(testShowtime);
        testSeat.setSeatRow("A");
        testSeat.setSeatNumber(1);
        testSeat.setSeatType("REGULAR");
        testSeat.setPrice(15.0);
        testSeat.setStatus("BOOKED");

        // Setup test payment
        testPayment = new Payment();
        testPayment.setId(1L);
        testPayment.setCardnumber("1234567890123456");
        testPayment.setCardname("John Doe");
        testPayment.setAmount(15.0);
        testPayment.setUserid(2);

        // Setup test user (registered user)
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setUserType("REGULAR");

        // Setup test ticket
        testTicket = new Ticket();
        testTicket.setId(1L);
        testTicket.setUserId(2);
        testTicket.setMovieId(1L);
        testTicket.setSeatId(1L);
        testTicket.setPaymentId(1L);
        testTicket.setPurchaseDate(LocalDateTime.now().minusDays(1));
        testTicket.setStatus(TicketStatus.ACTIVE);
        testTicket.setIsRefundable(true);
        testTicket.setReferenceNumber("REF00001");
    }

    @Test
    @DisplayName("Should cancel ticket successfully for registered user with full refund")
    void cancelTicket_RegisteredUser_FullRefund() {
        // Arrange
        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(userRepository.findById(2L)).thenReturn(Optional.of(testUser));

        // Act
        String result = cancellationService.cancelTicket("REF00001");

        // Assert
        assertThat(result).contains("Your ticket has been canceled successfully");
        assertThat(result).contains("$15.00");
        assertThat(result).contains("3456"); // Last 4 digits of card
        verify(ticketRepository).save(any(Ticket.class));
        verify(seatRepository).save(any(Seat.class));
        verify(creditRepository, never()).save(any(Credit.class)); // No credit for registered user
    }

    @Test
    @DisplayName("Should cancel ticket for guest user with 85% refund and 15% credit")
    void cancelTicket_GuestUser_PartialRefundWithCredit() {
        // Arrange
        testTicket.setUserId(1); // Guest user ID
        testPayment.setUserid(1);

        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        String result = cancellationService.cancelTicket("REF00001");

        // Assert
        assertThat(result).contains("Your ticket has been canceled successfully");
        assertThat(result).contains("$12.75"); // 85% refund
        assertThat(result).contains("$2.25"); // 15% credit
        assertThat(result).contains("credit code");

        // Verify credit was created
        ArgumentCaptor<Credit> creditCaptor = ArgumentCaptor.forClass(Credit.class);
        verify(creditRepository).save(creditCaptor.capture());
        Credit savedCredit = creditCaptor.getValue();
        assertThat(savedCredit.getAmount()).isEqualTo(2.25); // 15% of $15
        assertThat(savedCredit.getStatus()).isEqualTo(CreditStatus.UNUSED);
        assertThat(savedCredit.getCreditCode()).startsWith("REF00001");
    }

    @Test
    @DisplayName("Should update ticket status to CANCELED")
    void cancelTicket_UpdatesTicketStatus() {
        // Arrange
        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(userRepository.findById(2L)).thenReturn(Optional.of(testUser));

        // Act
        cancellationService.cancelTicket("REF00001");

        // Assert
        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketCaptor.capture());
        assertThat(ticketCaptor.getValue().getStatus()).isEqualTo(TicketStatus.CANCELED);
    }

    @Test
    @DisplayName("Should set seat status back to AVAILABLE")
    void cancelTicket_SetsSeatToAvailable() {
        // Arrange
        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(userRepository.findById(2L)).thenReturn(Optional.of(testUser));

        // Act
        cancellationService.cancelTicket("REF00001");

        // Assert
        ArgumentCaptor<Seat> seatCaptor = ArgumentCaptor.forClass(Seat.class);
        verify(seatRepository).save(seatCaptor.capture());
        assertThat(seatCaptor.getValue().getStatus()).isEqualTo("AVAILABLE");
    }

    @Test
    @DisplayName("Should throw TicketNotFoundException when ticket not found")
    void cancelTicket_TicketNotFound() {
        // Arrange
        when(ticketRepository.findByReferenceNumber("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cancellationService.cancelTicket("INVALID"))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("Invalid booking reference number");
    }

    @Test
    @DisplayName("Should throw TicketNotRefundableException when less than 72 hours before show")
    void cancelTicket_WithinSeventyTwoHours() {
        // Arrange
        // Set showtime to less than 72 hours from now
        testShowtime.setDate(LocalDate.now());
        testShowtime.setSession(3); // Evening (7 PM)

        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));

        // Act & Assert
        assertThatThrownBy(() -> cancellationService.cancelTicket("REF00001"))
                .isInstanceOf(TicketNotRefundableException.class)
                .hasMessageContaining("72 hours");
    }

    @Test
    @DisplayName("Should get ticket details successfully for authorized user")
    void getTicketDetails_Success() {
        // Arrange
        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));

        // Act
        Map<String, Object> details = cancellationService.getTicketDetails("REF00001", 2);

        // Assert
        assertThat(details.get("success")).isEqualTo(true);
        assertThat(details.get("movieTitle")).isEqualTo("Test Movie");
        assertThat(details.get("seatNumber")).isEqualTo("A1");
        assertThat(details.get("showTime")).isEqualTo("10:00 AM");
        assertThat(details.get("isRefundable")).isEqualTo(true);
        assertThat(details.get("status")).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("Should throw exception when guest user tries to access non-guest ticket")
    void getTicketDetails_GuestAccessDenied() {
        // Arrange - ticket belongs to user 2, but current user is guest (1)
        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));

        // Act & Assert
        assertThatThrownBy(() -> cancellationService.getTicketDetails("REF00001", 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not authorized");
    }

    @Test
    @DisplayName("Should throw exception when registered user tries to access another user's ticket")
    void getTicketDetails_RegisteredUserAccessDenied() {
        // Arrange - ticket belongs to user 2, but current user is user 3
        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));

        // Act & Assert
        assertThatThrownBy(() -> cancellationService.getTicketDetails("REF00001", 3))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not authorized");
    }

    @Test
    @DisplayName("Should correctly calculate refundability based on 72-hour rule")
    void getTicketDetails_RefundabilityCalculation() {
        // Arrange - showtime within 72 hours (not refundable)
        testShowtime.setDate(LocalDate.now().plusDays(1));
        testShowtime.setSession(1);

        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));

        // Act
        Map<String, Object> details = cancellationService.getTicketDetails("REF00001", 2);

        // Assert
        assertThat(details.get("isRefundable")).isEqualTo(false);
    }

    @Test
    @DisplayName("Should return correct show time format for each session")
    void getTicketDetails_ShowTimeFormats() {
        // Test morning session
        testShowtime.setSession(1);
        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));

        Map<String, Object> morningDetails = cancellationService.getTicketDetails("REF00001", 2);
        assertThat(morningDetails.get("showTime")).isEqualTo("10:00 AM");

        // Test afternoon session
        testShowtime.setSession(2);
        Map<String, Object> afternoonDetails = cancellationService.getTicketDetails("REF00001", 2);
        assertThat(afternoonDetails.get("showTime")).isEqualTo("2:00 PM");

        // Test evening session
        testShowtime.setSession(3);
        Map<String, Object> eveningDetails = cancellationService.getTicketDetails("REF00001", 2);
        assertThat(eveningDetails.get("showTime")).isEqualTo("7:00 PM");
    }

    @Test
    @DisplayName("Should throw TicketNotFoundException when getting details for non-existent ticket")
    void getTicketDetails_TicketNotFound() {
        // Arrange
        when(ticketRepository.findByReferenceNumber("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cancellationService.getTicketDetails("INVALID", 1))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    @DisplayName("Should handle canceled ticket correctly in details")
    void getTicketDetails_CanceledTicket() {
        // Arrange
        testTicket.setStatus(TicketStatus.CANCELED);

        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));

        // Act
        Map<String, Object> details = cancellationService.getTicketDetails("REF00001", 2);

        // Assert
        assertThat(details.get("status")).isEqualTo("CANCELED");
        assertThat(details.get("isRefundable")).isEqualTo(false); // Canceled tickets are not refundable
    }

    @Test
    @DisplayName("Should create credit with correct expiry date for guest user")
    void cancelTicket_GuestUser_CreditExpiryDate() {
        // Arrange
        testTicket.setUserId(1);
        testPayment.setUserid(1);

        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        cancellationService.cancelTicket("REF00001");

        // Assert
        ArgumentCaptor<Credit> creditCaptor = ArgumentCaptor.forClass(Credit.class);
        verify(creditRepository).save(creditCaptor.capture());
        Credit savedCredit = creditCaptor.getValue();
        assertThat(savedCredit.getExpiryDate()).isEqualTo(LocalDate.now().plusYears(1));
    }

    @Test
    @DisplayName("Should handle payment with null card number gracefully")
    void cancelTicket_NullCardNumber() {
        // Arrange
        testPayment.setCardnumber(null);

        when(ticketRepository.findByReferenceNumber("REF00001")).thenReturn(Optional.of(testTicket));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));
        when(userRepository.findById(2L)).thenReturn(Optional.of(testUser));

        // Act
        String result = cancellationService.cancelTicket("REF00001");

        // Assert
        assertThat(result).contains("****"); // Default masked card number
    }
}
