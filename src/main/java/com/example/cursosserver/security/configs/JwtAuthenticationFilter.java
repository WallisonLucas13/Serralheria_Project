package com.example.cursosserver.security.configs;

import com.example.cursosserver.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Autowired
    private UserDetailsService service;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("Request Chegou, aqui está o header: " + header);

        if(header == null || !header.startsWith(PREFIX)){
            filterChain.doFilter(request, response);
            System.out.println("Request Chegou, mas não tem o prefixo: " + header);
            return;
        }

        String token = header.substring(7);
        String username = jwtService.extractUsername(token);

        System.out.println("Header passou, aqui está o token: " + token);
        System.out.println("Header passou, aqui está o username: " + username);

        if(username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails user = service.loadUserByUsername(username);
            System.out.println("Username passou, aqui está o user: " + user);

            if(jwtService.isTokenValid(token, user)){

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );

                System.out.println("Token é válido, aqui está o auth: " + auth);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
