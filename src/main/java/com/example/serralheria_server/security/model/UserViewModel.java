package com.example.serralheria_server.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewModel {

    public UserViewModel(String username, String permissions){
        this.username = username;
        this.permissions = permissions;
    }

    private String username;

    private String permissions;
}
