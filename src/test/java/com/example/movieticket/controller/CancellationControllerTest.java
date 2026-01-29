package com.example.movieticket.controller;

import com.example.movieticket.config.SecurityConfig;
import com.example.movieticket.entity.User;
import com.example.movieticket.exception.TicketNotFoundException;
import com.example.movieticket.exception.TicketNotRefundableException;
import com.example.movieticket.repository.UserRepository;
import com.example.movieticket.service.CancellationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CancellationController.class)
@Import(SecurityConfig.class)
class CancellationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CancellationService cancellationService;

    @MockBean
    private UserRepository userRepository;

    private MockHttpSession session;
    private User testUser;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("username", "testuser");
        session.setAttribute("userId", 2);

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setUserType("REGULAR");
    }

    @Test
    @DisplayName("Should show cancellation page")
    void showCancellationPage() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(List.of(testUser));

        mockMvc.perform(get("/cancellation/cancel-ticket").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cancellation"))
                .andExpect(model().attribute("displayName", "testuser"))
                .andExpect(model().attribute("userEmail", "test@example.com"));
    }

    @Test
    @DisplayName("Should show cancellation page for ordinary user")
    void showCancellationPage_OrdinaryUser() throws Exception {
        session.setAttribute("username", "Ordinary User");

        mockMvc.perform(get("/cancellation/cancel-ticket").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cancellation"))
                .andExpect(model().attribute("displayName", "Ordinary User"))
                .andExpect(model().attributeDoesNotExist("userEmail"));
    }

    @Test
    @DisplayName("Should cancel ticket successfully")
    void cancelTicket_Success() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "REF00001");

        when(cancellationService.cancelTicket("REF00001"))
                .thenReturn("Your ticket has been canceled successfully. The refund amount $15.00 has been sent to your card ending 3456");

        mockMvc.perform(post("/cancellation/cancel-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Your ticket has been canceled successfully. The refund amount $15.00 has been sent to your card ending 3456"));
    }

    @Test
    @DisplayName("Should return 400 for empty booking reference")
    void cancelTicket_EmptyReference() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "");

        mockMvc.perform(post("/cancellation/cancel-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Booking reference is required."));
    }

    @Test
    @DisplayName("Should return 400 for missing booking reference")
    void cancelTicket_MissingReference() throws Exception {
        Map<String, String> request = new HashMap<>();

        mockMvc.perform(post("/cancellation/cancel-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Booking reference is required."));
    }

    @Test
    @DisplayName("Should return 404 for non-existent ticket")
    void cancelTicket_NotFound() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "INVALID");

        when(cancellationService.cancelTicket("INVALID"))
                .thenThrow(new TicketNotFoundException("Invalid booking reference number"));

        mockMvc.perform(post("/cancellation/cancel-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid booking reference number"));
    }

    @Test
    @DisplayName("Should return 400 when ticket not refundable")
    void cancelTicket_NotRefundable() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "REF00001");

        when(cancellationService.cancelTicket("REF00001"))
                .thenThrow(new TicketNotRefundableException("Sorry, you are not eligible to cancel this ticket."));

        mockMvc.perform(post("/cancellation/cancel-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Sorry, you are not eligible to cancel this ticket."));
    }

    @Test
    @DisplayName("Should return 500 for unexpected errors")
    void cancelTicket_UnexpectedError() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "REF00001");

        when(cancellationService.cancelTicket("REF00001"))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post("/cancellation/cancel-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error occurred: Database connection failed"));
    }

    @Test
    @DisplayName("Should search ticket successfully")
    void searchTicket_Success() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "REF00001");

        Map<String, Object> ticketDetails = new HashMap<>();
        ticketDetails.put("success", true);
        ticketDetails.put("movieTitle", "Iron Man");
        ticketDetails.put("showDate", "2026-02-01");
        ticketDetails.put("showTime", "10:00 AM");
        ticketDetails.put("seatNumber", "A1");
        ticketDetails.put("isRefundable", true);
        ticketDetails.put("status", "ACTIVE");

        when(cancellationService.getTicketDetails("REF00001", 2)).thenReturn(ticketDetails);

        mockMvc.perform(post("/cancellation/search-ticket")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.movieTitle").value("Iron Man"))
                .andExpect(jsonPath("$.seatNumber").value("A1"))
                .andExpect(jsonPath("$.isRefundable").value(true));
    }

    @Test
    @DisplayName("Should search ticket for ordinary user")
    void searchTicket_OrdinaryUser() throws Exception {
        session.setAttribute("username", "Ordinary User");
        session.setAttribute("userId", null);

        Map<String, String> request = Map.of("bookingReference", "REF00001");

        Map<String, Object> ticketDetails = new HashMap<>();
        ticketDetails.put("success", true);
        ticketDetails.put("movieTitle", "Iron Man");

        when(cancellationService.getTicketDetails("REF00001", 1)).thenReturn(ticketDetails);

        mockMvc.perform(post("/cancellation/search-ticket")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should return 404 when searching for non-existent ticket")
    void searchTicket_NotFound() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "INVALID");

        when(cancellationService.getTicketDetails(anyString(), anyInt()))
                .thenThrow(new TicketNotFoundException("Ticket not found"));

        mockMvc.perform(post("/cancellation/search-ticket")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Ticket not found with this reference number."));
    }

    @Test
    @DisplayName("Should return 400 for unauthorized access")
    void searchTicket_Unauthorized() throws Exception {
        Map<String, String> request = Map.of("bookingReference", "REF00001");

        when(cancellationService.getTicketDetails("REF00001", 2))
                .thenThrow(new RuntimeException("You are not authorized to view this ticket"));

        mockMvc.perform(post("/cancellation/search-ticket")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error: You are not authorized to view this ticket"));
    }
}
