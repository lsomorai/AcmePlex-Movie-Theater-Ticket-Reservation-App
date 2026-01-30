package com.example.movieticket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Integer userId;
    private String username;
    private String userType;
    private String email;
    private String accessToken;
    private String refreshToken;
}
