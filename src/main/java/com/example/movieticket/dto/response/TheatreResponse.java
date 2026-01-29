package com.example.movieticket.dto.response;

import com.example.movieticket.entity.Theatre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheatreResponse {

    private Long id;
    private String name;

    public static TheatreResponse fromEntity(Theatre theatre) {
        return TheatreResponse.builder()
                .id(theatre.getId())
                .name(theatre.getName())
                .build();
    }
}
