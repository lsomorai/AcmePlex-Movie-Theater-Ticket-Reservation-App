package com.example.movieticket.controller;

import com.example.movieticket.config.SecurityConfig;
import com.example.movieticket.entity.Movie;
import com.example.movieticket.entity.MovieStatus;
import com.example.movieticket.entity.Theatre;
import com.example.movieticket.repository.MovieRepository;
import com.example.movieticket.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@Import(SecurityConfig.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private TheatreRepository theatreRepository;

    private Theatre testTheatre;
    private Movie testMovie1;
    private Movie testMovie2;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        testTheatre = new Theatre();
        testTheatre.setId(1L); // Theatre uses long (primitive)
        testTheatre.setName("Scotiabank Chinook");

        testMovie1 = new Movie();
        testMovie1.setId(1L);
        testMovie1.setTitle("Iron Man");
        testMovie1.setStatus(MovieStatus.NOW_SHOWING);

        testMovie2 = new Movie();
        testMovie2.setId(2L);
        testMovie2.setTitle("Dune");
        testMovie2.setStatus(MovieStatus.COMING_SOON);

        session = new MockHttpSession();
    }

    @Test
    @DisplayName("Should show movies for a specific theatre")
    void showMovies_ForTheatre() throws Exception {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(testTheatre));
        when(movieRepository.findMoviesByTheatreId(1L)).thenReturn(List.of(testMovie1, testMovie2));

        session.setAttribute("username", "testuser");

        mockMvc.perform(get("/theatres/1/movies").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attributeExists("theatre"))
                .andExpect(model().attribute("displayName", "testuser"))
                .andExpect(model().attribute("isRegisteredUser", true));
    }

    @Test
    @DisplayName("Should identify registered user correctly")
    void showMovies_RegisteredUser() throws Exception {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(testTheatre));
        when(movieRepository.findMoviesByTheatreId(1L)).thenReturn(List.of(testMovie1));

        session.setAttribute("username", "testuser");

        mockMvc.perform(get("/theatres/1/movies").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isRegisteredUser", true));
    }

    @Test
    @DisplayName("Should identify ordinary user correctly")
    void showMovies_OrdinaryUser() throws Exception {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(testTheatre));
        when(movieRepository.findMoviesByTheatreId(1L)).thenReturn(List.of(testMovie1));

        session.setAttribute("username", "Ordinary User");

        mockMvc.perform(get("/theatres/1/movies").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isRegisteredUser", false))
                .andExpect(model().attribute("displayName", "Ordinary User"));
    }

    @Test
    @DisplayName("Should handle missing session username")
    void showMovies_NoUsername() throws Exception {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(testTheatre));
        when(movieRepository.findMoviesByTheatreId(1L)).thenReturn(List.of(testMovie1));

        mockMvc.perform(get("/theatres/1/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isRegisteredUser", false))
                .andExpect(model().attribute("displayName", "Ordinary User"));
    }

    @Test
    @DisplayName("Should store theatre ID in session")
    void showMovies_StoresTheatreIdInSession() throws Exception {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(testTheatre));
        when(movieRepository.findMoviesByTheatreId(1L)).thenReturn(List.of(testMovie1));

        mockMvc.perform(get("/theatres/1/movies").session(session))
                .andExpect(status().isOk());

        // Session should contain the theatre ID
        assert session.getAttribute("currentTheatreId").equals(1L);
    }

    @Test
    @DisplayName("Should handle non-existent theatre")
    void showMovies_TheatreNotFound() throws Exception {
        when(theatreRepository.findById(999L)).thenReturn(Optional.empty());
        when(movieRepository.findMoviesByTheatreId(999L)).thenReturn(List.of());

        mockMvc.perform(get("/theatres/999/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attribute("theatre", (Object) null));
    }

    @Test
    @DisplayName("Should list all movies")
    void listAllMovies() throws Exception {
        when(movieRepository.findAll()).thenReturn(List.of(testMovie1, testMovie2));

        session.setAttribute("username", "testuser");

        mockMvc.perform(get("/movies").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attributeExists("movies"))
                .andExpect(model().attribute("theatre", (Object) null))
                .andExpect(model().attribute("isRegisteredUser", true));
    }

    @Test
    @DisplayName("Should clear theatre ID from session when listing all movies")
    void listAllMovies_ClearsTheatreId() throws Exception {
        session.setAttribute("currentTheatreId", 1L);

        when(movieRepository.findAll()).thenReturn(List.of(testMovie1));

        mockMvc.perform(get("/movies").session(session))
                .andExpect(status().isOk());

        // Session should no longer contain theatre ID
        assert session.getAttribute("currentTheatreId") == null;
    }

    @Test
    @DisplayName("Should list all movies for ordinary user")
    void listAllMovies_OrdinaryUser() throws Exception {
        when(movieRepository.findAll()).thenReturn(List.of(testMovie1, testMovie2));

        session.setAttribute("username", "Ordinary User");

        mockMvc.perform(get("/movies").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isRegisteredUser", false))
                .andExpect(model().attribute("displayName", "Ordinary User"));
    }

    @Test
    @DisplayName("Should handle empty movie list")
    void showMovies_EmptyList() throws Exception {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(testTheatre));
        when(movieRepository.findMoviesByTheatreId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/theatres/1/movies"))
                .andExpect(status().isOk())
                .andExpect(view().name("movies"))
                .andExpect(model().attribute("movies", List.of()));
    }
}
