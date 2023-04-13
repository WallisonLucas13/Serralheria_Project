package com.example.cursosserver.security.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String token;
}
