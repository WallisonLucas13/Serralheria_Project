package com.example.cursosserver.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider provider;

    private final JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf()
                .disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                //.requestMatchers(HttpMethod.POST, "/api/login")
                .anyRequest()
                .permitAll()
                //.anyRequest()
                //.authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
