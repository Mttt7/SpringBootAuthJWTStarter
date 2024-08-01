package com.mt.jwtstarter.dto.Auth;


import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    private String password;
    private String passwordRepeated;
    private String firstName;
    private String lastName;
}

