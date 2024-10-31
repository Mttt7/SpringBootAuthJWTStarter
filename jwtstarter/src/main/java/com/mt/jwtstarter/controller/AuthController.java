package com.mt.jwtstarter.controller;

import com.mt.jwtstarter.dto.Auth.AuthResponseDto;
import com.mt.jwtstarter.dto.Auth.LoginDto;
import com.mt.jwtstarter.dto.Auth.RegisterDto;
import com.mt.jwtstarter.service.AuthService;
import com.mt.jwtstarter.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        return new ResponseEntity<>(authService.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterDto registerDto) {
        Map<String, String> response = authService.registerUser(registerDto);
        HttpStatus status = Objects.equals(response.get("message"), "success") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Map<String,Boolean>> checkUsernameAvailability(@PathVariable String username){
        if(username.isEmpty()) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("empty", true);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Boolean res = userService.checkUsernameAvailability(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", res);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String,Boolean>> checkEmailAvailability(@PathVariable String email){
        if(email.isEmpty()) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("empty", true);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Boolean res =  userService.checkEmailAvailability(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", res);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}