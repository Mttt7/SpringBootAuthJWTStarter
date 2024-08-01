package com.mt.jwtstarter.controller;



import com.mt.jwtstarter.config.security.JWTGenerator;
import com.mt.jwtstarter.dto.Auth.AuthResponseDto;
import com.mt.jwtstarter.dto.Auth.LoginDto;
import com.mt.jwtstarter.dto.Auth.RegisterDto;
import com.mt.jwtstarter.mapper.StringResponseMapper;
import com.mt.jwtstarter.mapper.UserMapper;
import com.mt.jwtstarter.model.Role;
import com.mt.jwtstarter.model.UserEntity;
import com.mt.jwtstarter.repository.RoleRepository;
import com.mt.jwtstarter.repository.UserRepository;
import com.mt.jwtstarter.service.AuthService;
import com.mt.jwtstarter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = authService.getLoggedUser();
        AuthResponseDto response = new AuthResponseDto(token);
        response.setUser(UserMapper.mapToUserResponseDto(user));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody RegisterDto registerDto){
        if(!Objects.equals(registerDto.getPassword(), registerDto.getPasswordRepeated())){
            return new ResponseEntity<>(StringResponseMapper.mapToMap("Passwords don't match"), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>(StringResponseMapper.mapToMap("Username is taken!"), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>(StringResponseMapper.mapToMap("Email is taken!"), HttpStatus.BAD_REQUEST);
        }else{
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(registerDto.getEmail());
            if(!matcher.matches()){
                return new ResponseEntity<>(StringResponseMapper.mapToMap("Wrong email! Email should look like: example@mail.com"), HttpStatus.BAD_REQUEST);
            }

        }
        if(registerDto.getFirstName().length()>25 ||
                registerDto.getFirstName().length()<3 ||
                registerDto.getLastName().length()>25 ||
                registerDto.getLastName().length()<3){

            return new ResponseEntity<>(StringResponseMapper.mapToMap("Bad Name!"), HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        //user.setPhotoUrl("https://firebasestorage.googleapis.com/");

        Role role = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);
        return new ResponseEntity<>(StringResponseMapper.mapToMap("success"), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Map<String,Boolean>> checkUsernameAvailability(@PathVariable String username){
        if(username.isEmpty()) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("empty", true);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Boolean res =  userService.checkUsernameAvailability(username);
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