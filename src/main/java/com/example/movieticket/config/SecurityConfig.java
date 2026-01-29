package com.example.movieticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - accessible without authentication
                .requestMatchers("/", "/login", "/register", "/signup", "/logout").permitAll()
                .requestMatchers("/dashboard").permitAll()
                .requestMatchers("/verify-username").permitAll()
                .requestMatchers("/RuPayment").permitAll()
                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                // H2 Console (development only)
                .requestMatchers("/h2-console/**").permitAll()
                // API endpoints
                .requestMatchers("/api/**").permitAll()
                // Swagger/OpenAPI
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Actuator health endpoints
                .requestMatchers("/actuator/**").permitAll()
                // Movie browsing - allow all for guest access
                .requestMatchers("/theatres/**", "/movies/**", "/showtimes/**", "/seats/**").permitAll()
                // Ticket and payment endpoints
                .requestMatchers("/ticket-confirmation", "/ticket-payment", "/process-ticket-payment").permitAll()
                .requestMatchers("/booking-success").permitAll()
                // Cancellation
                .requestMatchers("/cancellation/**").permitAll()
                // Admin endpoints require ADMIN role
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            // Disable form login - we handle login ourselves in AuthenticationController
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Disable CSRF for API endpoints, H2 console, and JSON-based controllers
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
                .ignoringRequestMatchers("/api/**")
                .ignoringRequestMatchers("/cancellation/**")
            )
            // Allow frames for H2 console
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
