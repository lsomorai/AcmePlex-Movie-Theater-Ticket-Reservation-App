package com.example.movieticket.service;

import com.example.movieticket.dto.PaymentRequest;
import com.example.movieticket.entity.*;
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
import java.util.Optional;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketPaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private CreditRepository creditRepository;

    private TicketPaymentService ticketPaymentService;

    private PaymentRequest paymentRequest;
    private Seat testSeat;
    private Showtime testShowtime;
    private Movie testMovie;
    private Payment savedPayment;

    @BeforeEach
    void setUp() throws Exception {
        // Manually create TicketPaymentService and inject mocks
        ticketPaymentService = new TicketPaymentService(paymentRepository, ticketRepository, seatRepository);

        // Use reflection to inject the @Autowired creditRepository
        Field creditRepoField = TicketPaymentService.class.getDeclaredField("creditRepository");
        creditRepoField.setAccessible(true);
        creditRepoField.set(ticketPaymentService, creditRepository);

        // Setup test movie
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setStatus(MovieStatus.NOW_SHOWING);

        // Setup test showtime
        testShowtime = new Showtime();
        testShowtime.setId(1L);
        testShowtime.setMovie(testMovie);
        testShowtime.setDate(LocalDate.now().plusDays(5));
        testShowtime.setSession(1);

        // Setup test seat
        testSeat = new Seat();
        testSeat.setId(1L);
        testSeat.setTheaterId(1);
        testSeat.setShowtime(testShowtime);
        testSeat.setSeatRow("A");
        testSeat.setSeatNumber(1);
        testSeat.setSeatType("REGULAR");
        testSeat.setPrice(15.0);
        testSeat.setStatus("AVAILABLE");

        // Setup payment request
        paymentRequest = new PaymentRequest();
        paymentRequest.setCardnumber("1234567890123456");
        paymentRequest.setCardname("John Doe");
        paymentRequest.setExpirydate("12/25");
        paymentRequest.setCvv("123");
        paymentRequest.setAmount(15.0);
        paymentRequest.setSelectedSeats("1");
        paymentRequest.setShowtimeId(1L);

        // Setup saved payment
        savedPayment = new Payment();
        savedPayment.setId(1L);
        savedPayment.setCardnumber("1234567890123456");
        savedPayment.setCardname("John Doe");
        savedPayment.setExpirydate("12/25");
        savedPayment.setCvv("123");
        savedPayment.setAmount(15.0);
        savedPayment.setUserid(1);
        savedPayment.setNote("TICKET_PURCHASE");
    }

    @Test
    @DisplayName("Should process payment and create ticket successfully")
    void processTicketPayment_Success() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(ticketRepository.findTopByOrderByReferenceNumberDesc()).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String referenceNumber = ticketPaymentService.processTicketPayment(paymentRequest, 1);

        // Assert
        assertThat(referenceNumber).isEqualTo("REF00001");
        verify(paymentRepository).save(any(Payment.class));
        verify(ticketRepository).save(any(Ticket.class));
        verify(seatRepository).save(any(Seat.class));
    }

    @Test
    @DisplayName("Should generate sequential reference numbers")
    void processTicketPayment_GeneratesSequentialReferenceNumbers() {
        // Arrange
        Ticket existingTicket = new Ticket();
        existingTicket.setReferenceNumber("REF00005");

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(ticketRepository.findTopByOrderByReferenceNumberDesc()).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String referenceNumber = ticketPaymentService.processTicketPayment(paymentRequest, 1);

        // Assert
        assertThat(referenceNumber).isEqualTo("REF00006");
    }

    @Test
    @DisplayName("Should update seat status to BOOKED after payment")
    void processTicketPayment_UpdatesSeatStatus() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(ticketRepository.findTopByOrderByReferenceNumberDesc()).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ticketPaymentService.processTicketPayment(paymentRequest, 1);

        // Assert
        ArgumentCaptor<Seat> seatCaptor = ArgumentCaptor.forClass(Seat.class);
        verify(seatRepository).save(seatCaptor.capture());
        assertThat(seatCaptor.getValue().getStatus()).isEqualTo("BOOKED");
    }

    @Test
    @DisplayName("Should process multiple seats in single payment")
    void processTicketPayment_MultipleSeats() {
        // Arrange
        Seat testSeat2 = new Seat();
        testSeat2.setId(2L);
        testSeat2.setTheaterId(1);
        testSeat2.setShowtime(testShowtime);
        testSeat2.setSeatRow("A");
        testSeat2.setSeatNumber(2);
        testSeat2.setSeatType("REGULAR");
        testSeat2.setPrice(15.0);
        testSeat2.setStatus("AVAILABLE");

        paymentRequest.setSelectedSeats("1,2");
        paymentRequest.setAmount(30.0);

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(seatRepository.findById(2L)).thenReturn(Optional.of(testSeat2));
        when(ticketRepository.findTopByOrderByReferenceNumberDesc()).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String referenceNumber = ticketPaymentService.processTicketPayment(paymentRequest, 1);

        // Assert
        assertThat(referenceNumber).isNotNull();
        verify(ticketRepository, times(2)).save(any(Ticket.class));
        verify(seatRepository, times(2)).save(any(Seat.class));
    }

    @Test
    @DisplayName("Should apply and mark credit as used")
    void processTicketPayment_WithCreditCode() {
        // Arrange
        Credit credit = new Credit();
        credit.setId(1L);
        credit.setCreditCode("CREDIT123");
        credit.setAmount(5.0);
        credit.setExpiryDate(LocalDate.now().plusDays(30));
        credit.setStatus(CreditStatus.UNUSED);

        paymentRequest.setAppliedCreditCode("CREDIT123");
        paymentRequest.setAppliedCreditAmount(5.0);
        paymentRequest.setAmount(10.0);

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(ticketRepository.findTopByOrderByReferenceNumberDesc()).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(creditRepository.findByCreditCodeAndStatusAndExpiryDateGreaterThanEqual(
                eq("CREDIT123"), eq(CreditStatus.UNUSED), any(LocalDate.class)))
                .thenReturn(Optional.of(credit));
        when(creditRepository.save(any(Credit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ticketPaymentService.processTicketPayment(paymentRequest, 1);

        // Assert
        ArgumentCaptor<Credit> creditCaptor = ArgumentCaptor.forClass(Credit.class);
        verify(creditRepository).save(creditCaptor.capture());
        assertThat(creditCaptor.getValue().getStatus()).isEqualTo(CreditStatus.USED);
    }

    @Test
    @DisplayName("Should throw exception when seat not found")
    void processTicketPayment_SeatNotFound() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> ticketPaymentService.processTicketPayment(paymentRequest, 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Seat not found");
    }

    @Test
    @DisplayName("Should set correct ticket properties")
    void processTicketPayment_SetsCorrectTicketProperties() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(ticketRepository.findTopByOrderByReferenceNumberDesc()).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ticketPaymentService.processTicketPayment(paymentRequest, 2);

        // Assert
        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketCaptor.capture());

        Ticket savedTicket = ticketCaptor.getValue();
        assertThat(savedTicket.getUserId()).isEqualTo(2);
        assertThat(savedTicket.getMovieId()).isEqualTo(1L);
        assertThat(savedTicket.getSeatId()).isEqualTo(1L);
        assertThat(savedTicket.getPaymentId()).isEqualTo(1L);
        assertThat(savedTicket.getStatus()).isEqualTo(TicketStatus.ACTIVE);
        assertThat(savedTicket.getIsRefundable()).isTrue();
        assertThat(savedTicket.getPurchaseDate()).isNotNull();
    }

    @Test
    @DisplayName("Should save payment with correct note")
    void processTicketPayment_SavesPaymentWithCorrectNote() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(testSeat));
        when(ticketRepository.findTopByOrderByReferenceNumberDesc()).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ticketPaymentService.processTicketPayment(paymentRequest, 1);

        // Assert
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        assertThat(paymentCaptor.getValue().getNote()).isEqualTo("TICKET_PURCHASE");
    }
}
