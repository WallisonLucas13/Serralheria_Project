package com.example.cursosserver.security.dto;

import com.example.cursosserver.security.model.UserModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String key;

    public UserModel toUser(){
        return new UserModel(username, password, key);
    }
}
