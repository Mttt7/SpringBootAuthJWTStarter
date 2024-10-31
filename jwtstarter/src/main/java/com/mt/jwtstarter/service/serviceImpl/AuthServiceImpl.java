package com.mt.jwtstarter.service.serviceImpl;

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
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public UserEntity getLoggedUser() {
        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = getLoggedUser();
        AuthResponseDto response = new AuthResponseDto(token);
        response.setUser(UserMapper.mapToUserResponseDto(user));
        return response;
    }

    @Override
    public Map<String, String> registerUser(RegisterDto registerDto) {
        if (!Objects.equals(registerDto.getPassword(), registerDto.getPasswordRepeated())) {
            return StringResponseMapper.mapToMap("Passwords don't match");
        }
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return StringResponseMapper.mapToMap("Username is taken!");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return StringResponseMapper.mapToMap("Email is taken!");
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(registerDto.getEmail());
        if (!matcher.matches()) {
            return StringResponseMapper.mapToMap("Wrong email! Email should look like: example@mail.com");
        }

        if (registerDto.getFirstName().length() > 25 || registerDto.getFirstName().length() < 3 ||
                registerDto.getLastName().length() > 25 || registerDto.getLastName().length() < 3) {
            return StringResponseMapper.mapToMap("Bad Name!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);
        return StringResponseMapper.mapToMap("success");
    }
}
