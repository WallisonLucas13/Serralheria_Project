package com.example.serralheria_server.security.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String token;
}
