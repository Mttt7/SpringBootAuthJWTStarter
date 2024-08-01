package com.mt.jwtstarter.dto.Auth;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
