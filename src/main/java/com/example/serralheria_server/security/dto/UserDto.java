package com.example.serralheria_server.security.dto;

import com.example.serralheria_server.security.model.UserModel;
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

    private String chaveAccess;

    private String writePermission;

    public UserModel toUser(){
        return new UserModel(username, password);
    }
}
