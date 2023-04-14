package com.example.cursosserver.security.controller;

import com.example.cursosserver.security.dto.UserDto;
import com.example.cursosserver.security.model.AuthenticationResponse;
import com.example.cursosserver.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserDto dto){

        System.out.println("Login");
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.login(dto.toUser()));
        }
        catch(IllegalArgumentException e){
            System.out.println("Acesso Negado!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login/access")
    public ResponseEntity<AuthenticationResponse> loginWithToken(@RequestBody AuthenticationResponse auth){

        System.out.println("Login");
        try{
            if(service.loginWithToken(auth)){
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch(IllegalArgumentException e){
            System.out.println("Acesso Negado!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDto dto){

        try{
            AuthenticationResponse res = service.register(dto.toUser());
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
