package com.example.movieticket.controller;

import com.example.movieticket.entity.User;
import com.example.movieticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import com.example.movieticket.config.SecurityConfig;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfig.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setUserType("REGULAR");

        session = new MockHttpSession();
    }

    @Test
    @DisplayName("Should show login page")
    void showLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("Should show login page with return URL")
    void showLoginPage_WithReturnUrl() throws Exception {
        mockMvc.perform(get("/").param("returnUrl", "/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("returnUrl", "/movies"));
    }

    @Test
    @DisplayName("Should show register page")
    void showRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @DisplayName("Should login successfully and redirect to dashboard")
    void login_Success() throws Exception {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(List.of(testUser));

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DisplayName("Should login and redirect to return URL if provided")
    void login_WithReturnUrl() throws Exception {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(List.of(testUser));

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("returnUrl", "/movies"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));
    }

    @Test
    @DisplayName("Should show error on invalid credentials")
    void login_InvalidCredentials() throws Exception {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "wronguser")
                        .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("isError", true))
                .andExpect(model().attribute("errorMessage", "Invalid username or password"));
    }

    @Test
    @DisplayName("Should signup with valid credentials and redirect to payment")
    void signup_Success() throws Exception {
        when(userRepository.findByUsername("newuser")).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "newuser")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment"))
                .andExpect(model().attribute("username", "newuser"))
                .andExpect(model().attribute("amount", "20.00"));
    }

    @Test
    @DisplayName("Should reject signup with short username")
    void signup_ShortUsername() throws Exception {
        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "abc")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("isError", true))
                .andExpect(model().attribute("errorMessage", "Username must be at least 5 characters long"));
    }

    @Test
    @DisplayName("Should reject signup with short password")
    void signup_ShortPassword() throws Exception {
        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "newuser")
                        .param("password", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("isError", true))
                .andExpect(model().attribute("errorMessage", "Password must be at least 5 characters long"));
    }

    @Test
    @DisplayName("Should reject signup with existing username")
    void signup_UsernameExists() throws Exception {
        when(userRepository.findByUsername("existinguser")).thenReturn(List.of(testUser));

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "existinguser")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("isError", true))
                .andExpect(model().attribute("errorMessage", "Username already exists"));
    }

    @Test
    @DisplayName("Should logout and invalidate session")
    void logout() throws Exception {
        session.setAttribute("username", "testuser");
        session.setAttribute("userId", 1);

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("Should verify username availability - available")
    void verifyUsername_Available() throws Exception {
        when(userRepository.findByUsername("newuser")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/verify-username").param("username", "newuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    @DisplayName("Should verify username availability - taken")
    void verifyUsername_Taken() throws Exception {
        when(userRepository.findByUsername("existinguser")).thenReturn(List.of(testUser));

        mockMvc.perform(get("/verify-username").param("username", "existinguser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DisplayName("Should verify username availability - too short")
    void verifyUsername_TooShort() throws Exception {
        mockMvc.perform(get("/verify-username").param("username", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    @DisplayName("Should show dashboard for logged in user")
    void showDashboard_LoggedIn() throws Exception {
        session.setAttribute("username", "testuser");

        mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("displayName", "testuser"));
    }

    @Test
    @DisplayName("Should show dashboard for guest user")
    void showDashboard_Guest() throws Exception {
        mockMvc.perform(get("/dashboard").param("guest", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("displayName", "Ordinary User"));
    }

    @Test
    @DisplayName("Should redirect to login when not authenticated")
    void showDashboard_NotAuthenticated() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
