package com.mt.jwtstarter.service;

import com.mt.jwtstarter.dto.Auth.AuthResponseDto;
import com.mt.jwtstarter.dto.Auth.LoginDto;
import com.mt.jwtstarter.dto.Auth.RegisterDto;
import com.mt.jwtstarter.model.UserEntity;
import java.util.Map;

public interface AuthService {

    UserEntity getLoggedUser();

    AuthResponseDto login(LoginDto loginDto);

    Map<String, String> registerUser(RegisterDto registerDto);
}
