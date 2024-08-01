package com.mt.jwtstarter.service.serviceImpl;



import com.mt.jwtstarter.model.UserEntity;
import com.mt.jwtstarter.repository.UserRepository;
import com.mt.jwtstarter.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    @Override
    public UserEntity getLoggedUser() {
        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(username).orElseThrow(
                ()-> new UsernameNotFoundException("User not found"));
        return user;
    }
}
