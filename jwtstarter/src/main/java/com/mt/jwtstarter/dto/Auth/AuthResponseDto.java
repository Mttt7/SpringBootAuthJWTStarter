package com.mt.jwtstarter.dto.Auth;



import com.mt.jwtstarter.model.UserEntity;
import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokenType="Bearer ";
    private UserResponseDto user;

    public AuthResponseDto(String accessToken){
        this.accessToken = accessToken;
    }
}
