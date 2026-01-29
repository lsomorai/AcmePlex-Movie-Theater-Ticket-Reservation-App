package com.example.movieticket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI acmePlexOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AcmePlex Movie Theater API")
                        .description("REST API for the AcmePlex Movie Theater Ticket Reservation System. " +
                                "This API provides endpoints for browsing movies, theatres, showtimes, " +
                                "purchasing tickets, and managing bookings.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("AcmePlex Team")
                                .email("support@acmeplex.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ));
    }
}
