package com.example.cursosserver.security.service;

import com.example.cursosserver.security.enums.RoleName;
import com.example.cursosserver.security.model.AuthenticationResponse;
import com.example.cursosserver.security.model.RoleModel;
import com.example.cursosserver.security.model.UserModel;
import com.example.cursosserver.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(UserModel userModel){

        boolean exist = repository.existsByUsername(userModel.getUsername());

        if(exist){
            throw new RuntimeException();
        }

        userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
        userModel.setRoles(List.of(generateRoleUser()));

        repository.save(userModel);

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(userModel))
                .build();

    }

    @Transactional
    public AuthenticationResponse login(UserModel model) throws IllegalArgumentException{

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    model.getUsername(),
                    model.getPassword()
                )
        );

        UserModel userModel = repository.findByUsername(model.getUsername())
                .orElseThrow(IllegalArgumentException::new);

        return AuthenticationResponse
                .builder()
                .token(jwtService.generateToken(userModel))
                .build();
    }


    private RoleModel generateRoleUser(){
        RoleModel roleModel = new RoleModel();
        roleModel.setRoleName(RoleName.ROLE_USER);
        return roleModel;
    }

    private RoleModel generateRoleAdmin(){
        RoleModel roleModel = new RoleModel();
        roleModel.setRoleName(RoleName.ROLE_ADMIN);
        return roleModel;
    }
}
